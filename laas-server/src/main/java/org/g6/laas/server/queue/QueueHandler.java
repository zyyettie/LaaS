package org.g6.laas.server.queue;

import org.g6.laas.server.database.entity.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@Configuration
public class QueueHandler {
    @Autowired
    private JobQueue queue;
    @Autowired
    private JobHelper jobHelper;

    @Scheduled(cron = "0/20 * *  * * ? ")
    public synchronized void handle() {
        System.out.println("Start handling all pending tasks in queue");

        while (!queue.isEmpty()) {
            System.out.println(queue.size());
            QueueJob queueJob = queue.get();

            if (queueJob.isDone()) {
                handleQueueTasks(queueJob);
                queue.remove();
            }
        }
    }

    private void handleQueueTasks(QueueJob queueJob) {
        Map<Task, QueueTask> queueTasks = queueJob.getQueueTasks();

        for (Map.Entry<Task, QueueTask> entry : queueTasks.entrySet()) {
            Task task = entry.getKey();
            QueueTask queueTask = entry.getValue();
            try {
                Object object = queueTask.getRunningResult();
                //TODO handle the return result and generate report
            } catch (ExecutionException e) {
                //TODO
            } catch (InterruptedException e) {
                //TODO
            }
        }

    }

}
