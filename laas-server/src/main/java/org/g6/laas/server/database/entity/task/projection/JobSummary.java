package org.g6.laas.server.database.entity.task.projection;

import org.g6.laas.server.database.entity.Job;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "jobSummary", types = { Job.class })
public interface JobSummary {
    Long getId();
    String getName();
    String getDescription();
    String getJobDate();
    String getScenario();
}
