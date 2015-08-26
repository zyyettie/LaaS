package org.g6.laas.server.database.entity.task.projection;

import org.g6.laas.server.database.entity.task.Task;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "taskSummary", types = { Task.class })
public interface TaskSummary {
    Long getId();
    String getName();
}