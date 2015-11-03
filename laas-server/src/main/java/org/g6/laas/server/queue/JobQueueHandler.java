package org.g6.laas.server.queue;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.service.JobService;
import org.g6.laas.server.vo.FileInfo;
import org.g6.laas.server.vo.TaskRunningResult;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class JobQueueHandler implements InitializingBean {
    @Autowired
    private JobQueue queue;
    @Autowired
    private JobService jobService;

    public boolean shutdown;

    @PostConstruct
    public void handle() {
        log.debug("Start handling all pending tasks in queue");
        new Executor().start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<JobRunning> jobRunnings = jobService.findUnFinishedJobInQueue("N", "RUNNING");
        for(JobRunning jobRunning : jobRunnings){
            jobService.runTasks(jobRunning);
        }
    }

    class Executor extends Thread {
        public void run() {
            while (!shutdown) {
                QueueJob queueJob = null;
                try {
                    queueJob = queue.get();
                } catch (InterruptedException e) {
                    log.error("error while getting job from queue", e);
                }

                if (queueJob != null) {
                    if (queueJob.isDone()) {
                        handleTasksInQueue(queueJob);
                    } else {
                        //if the current job is still running, need to append it in queue again
                        queue.addJob(queueJob);
                    }
                }
            }
        }
    }

    private void handleTasksInQueue(QueueJob queueJob) {
        Map<TaskRunning, QueueTask> queueTasks = queueJob.getQueueTasks();

        for (Map.Entry<TaskRunning, QueueTask> entry : queueTasks.entrySet()) {
            TaskRunning taskRunning = entry.getKey();
            QueueTask queueTask = entry.getValue();
            try {
                Object object = queueTask.getRunningResult();
                TaskRunningResult result = new TaskRunningResult();
                result.setResult(object);
                String report = jobService.genReport(result, taskRunning.getTask());
                FileInfo resultFile = jobService.writeReportToFile(report);

                jobService.handleResultFile(taskRunning, resultFile);
                taskRunning.setStatus("SUCCESS");
            } catch (ExecutionException e) {
                taskRunning.setStatus("FAILED");
                taskRunning.setRootCause(e.getMessage());
            } catch (InterruptedException e) {
                taskRunning.setStatus("FAILED");
                taskRunning.setRootCause(e.getMessage());
            }

            jobService.saveTaskRunning(taskRunning);
        }

        updateJobRunningStatus(queueJob.getJobRunning());
    }

    /**
     * When the status of a task running is set, need to check if the status of job running should be changed accordingly
     *
     * @param jobRunning
     */
    private void updateJobRunningStatus(JobRunning jobRunning) {
        Collection<TaskRunning> taskRunnings = jobRunning.getTaskRunnings();
        int runningCount = 0, successCount = 0, failCount = 0;
        int taskSize = taskRunnings.size();

        for (Iterator<TaskRunning> ite = taskRunnings.iterator(); ite.hasNext(); ) {
            TaskRunning tr = ite.next();

            if (tr.getStatus().equals("RUNNING")) {
                runningCount++;
            } else if (tr.getStatus().equals("FAILED")) {
                failCount++;
            } else if (tr.getStatus().equals("SUCCESS")) {
                successCount++;
            }
        }

        if (runningCount > 0) {
            //do nothing, need to wait for other tasks
        } else {
            if (failCount == taskSize) {
                jobRunning.setStatus("FAILED");
            } else if (successCount == taskSize) {
                jobRunning.setStatus("SUCCESS");
            } else if (failCount > 0 && successCount > 0) {
                jobRunning.setStatus("PARTIALLY SUCCESS");
            }

            makeJobRunningNotifiable(jobRunning);
            jobService.saveJobRunning(jobRunning);
        }
    }

    private void makeJobRunningNotifiable(JobRunning jobRunning) {
        Job job = jobRunning.getJob();
        String summary = job.getId() + " " + job.getName() + " <a.class=\"jobRunning\" href=\"/jobRunnings/"+ jobRunning.getId() + "/result\">Running Result</a>";
        jobRunning.setSummary(summary);
    }

    public void shutDown() {
        shutdown = true;
    }
}