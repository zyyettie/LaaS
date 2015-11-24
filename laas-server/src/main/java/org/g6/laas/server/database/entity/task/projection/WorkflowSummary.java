package org.g6.laas.server.database.entity.task.projection;

import org.g6.laas.server.database.entity.task.Workflow;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "workflowSummary", types = { Workflow.class })
public interface WorkflowSummary {
    Long getId();
    String getName();
    String getDescription();
}
