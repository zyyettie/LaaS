package org.g6.laas.server.queue;

import lombok.Data;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;

import java.util.HashMap;
import java.util.Map;

@Data
public class QueueJob {
    JobRunning jobRunning;
    Map<TaskRunning, QueueTask> queueTasks = new HashMap();

    public void addQueueTask(TaskRunning taskRunning, QueueTask queueTask) {
        queueTasks.put(taskRunning, queueTask);
    }

    public boolean isDone() {
        boolean isDone = true;
        for (QueueTask queueTask : queueTasks.values()) {
            if (!queueTask.isDone())
                isDone = false;
        }

        return isDone;
    }
}
