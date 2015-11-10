package org.g6.laas.server.database.entity.task.projection;

import org.g6.laas.server.database.entity.JobRunning;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "JobRunningSummary", types = { JobRunning.class })
public interface JobRunningSummary {
    Long getId();
    String getJobName();
    String getStatus();
    String getJobDescription();
    String getUser();
    String getRunningTime();
}
