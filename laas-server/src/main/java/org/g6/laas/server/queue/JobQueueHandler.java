package org.g6.laas.server.queue;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.g6.laas.server.database.entity.File;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.result.TaskResult;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class JobQueueHandler {
    @Autowired
    private JobQueue queue;
    @Autowired
    private JobHelper jobHelper;

    public boolean shutdown;

    @PostConstruct
    public void handle() {
        System.out.println("Start handling all pending tasks in queue");
        new Executor().start();
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
                        handleQueueTasks(queueJob);
                    } else {
                        //if the current job is still running, need to append it in queue again
                        queue.addJob(queueJob);
                    }
                }
            }
        }
    }

    private void handleQueueTasks(QueueJob queueJob) {
        Map<TaskRunning, QueueTask> queueTasks = queueJob.getQueueTasks();

        for (Map.Entry<TaskRunning, QueueTask> entry : queueTasks.entrySet()) {
            TaskRunning taskRunning = entry.getKey();
            QueueTask queueTask = entry.getValue();
            try {
                Object object = queueTask.getRunningResult();
                String report = getReport(object);
                //TODO need to discuss where the file should be placed
                String path = "";
                String fileName = "";

                File file = writeReportToFile(path, fileName, report);
                TaskResult taskResult = new TaskResult();
                Collection<File> files = new ArrayList<>();
                files.add(file);
                taskResult.setFiles(files);

                taskRunning.setResult(taskResult);
                taskRunning.setStatus("SUCCESS");
                jobHelper.saveTaskRunning(taskRunning);

            } catch (ExecutionException e) {
                //TODO
            } catch (InterruptedException e) {
                //TODO
            } catch (IOException ioe) {
                //TODO
            }
        }

        updateJobRunningStatus(queueJob.getJobRunning());
    }

    private String getReport(Object object) {
        //TODO
        return null;
    }

    private File writeReportToFile(String path, String fileName, String report) throws IOException {
        FileUtils.writeStringToFile(new java.io.File(path + fileName), report);

        File f = new File();
        f.setPath(path);
        f.setFileName(fileName);
        f.setOriginalName(fileName);

        return f;
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
        } else if (failCount == taskSize) {
            jobRunning.setStatus("FAILED");
            jobHelper.saveJobRunning(jobRunning);
        } else if (successCount == taskSize) {
            jobRunning.setStatus("SUCCESS");
            jobHelper.saveJobRunning(jobRunning);
        } else if (failCount > 0 && successCount > 0) {
            jobRunning.setStatus("PARTIALLY SUCCESS");
            jobHelper.saveJobRunning(jobRunning);
        }
    }

    public void shutDown() {
        shutdown = true;
    }
}