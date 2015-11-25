package org.g6.laas.server.queue;

import lombok.Data;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.ScenarioRunning;

import java.util.HashMap;
import java.util.Map;

@Data
public class QueueJob {
    JobRunning jobRunning;
    Map<ScenarioRunning, QueueTask> queueTasks = new HashMap();

    public void addQueueTask(ScenarioRunning taskRunning, QueueTask queueTask) {
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
