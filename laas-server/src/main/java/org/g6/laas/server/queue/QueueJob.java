package org.g6.laas.server.queue;

import lombok.Data;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.ScenarioRunning;

import java.util.HashMap;
import java.util.Map;

@Data
public class QueueJob {
    JobRunning jobRunning;
    Map<ScenarioRunning, QueueScenario> queueScenarios = new HashMap();

    public void addQueueScenario(ScenarioRunning scenarioRunning, QueueScenario queueScenario) {
        queueScenarios.put(scenarioRunning, queueScenario);
    }

    public boolean isDone() {
        boolean isDone = true;
        for (QueueScenario queueScenario : queueScenarios.values()) {
            if (!queueScenario.isDone())
                isDone = false;
        }

        return isDone;
    }
}
