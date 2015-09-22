package org.g6.laas.server.queue;

import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.repository.IJobRunningRepository;
import org.g6.laas.server.database.repository.ITaskRunningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobHelper {
    @Autowired
    private IJobRunningRepository jobRunningRepo;
    @Autowired
    private ITaskRunningRepository taskRunningRepo;

    public void saveTaskRunningStatus(TaskRunning taskRunning, String status) {
        taskRunning.setStatus(status);
        taskRunningRepo.save(taskRunning);
    }

    public void saveJobRunningStatus(JobRunning jobRunning, String status) {
        jobRunning.setStatus(status);
        jobRunningRepo.save(jobRunning);
    }

    public JobRunning saveJobRunning(JobRunning jobRunning){
        return jobRunningRepo.save(jobRunning);
    }

    public TaskRunning saveTaskRunning(TaskRunning taskRunning){
        return taskRunningRepo.save(taskRunning);
    }
}
