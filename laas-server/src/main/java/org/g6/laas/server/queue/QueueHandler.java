package org.g6.laas.server.queue;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.result.TaskResult;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import org.g6.laas.server.database.entity.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Configuration
@Slf4j
public class QueueHandler {
    @Autowired
    private JobQueue queue;
    @Autowired
    private JobHelper jobHelper;

    @Scheduled(cron = "0/20 * *  * * ? ")
    public synchronized void handle() {
        System.out.println("Start handling all pending tasks in queue");

        while (!queue.isEmpty()) {
            QueueJob queueJob = null;
            try {
                queueJob = queue.get();
            } catch (InterruptedException e) {
                log.error("error while getting job from queue", e);
                queue.remove();
            }

            if (queueJob.isDone()) {
                handleQueueTasks(queueJob);
            } else {
                //if the current job is still running, need to put it in queue again
                queue.addJob(queueJob);
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
     * @param taskRunning
     */
    private void updateJobRunningStatus(JobRunning jobRunning, TaskRunning taskRunning) {
        Collection<TaskRunning> taskRunnings = jobRunning.getTaskRunnings();

        for(Iterator<TaskRunning> ite = taskRunnings.iterator(); ite.hasNext();){

        }
    }
}
