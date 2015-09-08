package org.g6.laas.server.database.entity.task.projection;

import org.g6.laas.server.database.entity.task.Scenario;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "scenarioSummary", types = { Scenario.class })
public interface ScenarioSummary {
    Long getId();
    String getName();
}
