package org.g6.laas.server.controllers;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.server.database.entity.File;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.Scenario;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.service.JobService;
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
public class JobController {
    @Autowired
    private JobService jobService;

    @RequestMapping(value = "/controllers/jobs/{jobId}")
    ResponseEntity<String> runJob(@PathVariable Long jobId) {
        Job job = jobService.findJobBy(jobId);
        JobRunning jobRunning = createRunningRecords4JobAndTask(job);

        JobService.JobRunningResult runningResult = jobService.runTasks(jobRunning);

        Map<String, Object> jsonMap = new HashMap();
        jsonMap.put("job_id", String.valueOf(job.getId()));
        jsonMap.put("job_running_id", String.valueOf(jobRunning.getId()));
        jsonMap.put("is_syn", runningResult.isSyn());
        jsonMap.put("success", runningResult.isSuccess());
        jsonMap.put("rootcauses", runningResult.getRootCauses());

        return new ResponseEntity(JSONUtil.toJson(jsonMap), HttpStatus.OK);
    }

    /**
     * Insert JobRunning and TaskRunning records into database.
     * The status of JobRunning is set to "running" before all tasks are completed
     *
     * @param job
     * @return JobRunning object for JobRunning
     */
    private JobRunning createRunningRecords4JobAndTask(Job job) {
        JobRunning jobRunning = new JobRunning();
        jobRunning.setJob(job);
        jobRunning.setParameters(job.getParameters());
        jobRunning.setFiles(getFiles(job.getFiles()));
        jobRunning.setStatus("RUNNING");

        Collection<Scenario> scenarios = job.getScenarios();
        Collection<TaskRunning> taskRunnings = new ArrayList<>();
        Collection<Task> tasks;

        for (Iterator<Scenario> it = scenarios.iterator(); it.hasNext(); ) {
            Scenario scenario = it.next();
            tasks = scenario.getTasks();
            for (Iterator<Task> ite = tasks.iterator(); ite.hasNext(); ) {
                Task task = ite.next();
                TaskRunning taskRunning = new TaskRunning();
                taskRunning.setStatus("RUNNING");
                taskRunning.setTask(task);
                taskRunnings.add(taskRunning);
            }
        }

        jobRunning.setTaskRunnings(taskRunnings);
        JobRunning retJobRunning = jobService.saveJobRunning(jobRunning);

        return retJobRunning;
    }

    private Collection<File> getFiles(Collection<File> files) {
        Collection<File> fileList = new ArrayList<>();
        for (Iterator<File> ite = files.iterator(); ite.hasNext(); ) {
            File file = ite.next();
            fileList.add(file);
        }
        return fileList;
    }

    @RequestMapping(value = "/controllers/jobRunnings/{id}/result")
    ResponseEntity<String> getJobRunningResult(@PathVariable Long id) {
        JobRunning jobRunning = jobService.findJobRunningBy(id);
        Collection<TaskRunning> taskRunnings = jobRunning.getTaskRunnings();
        Map<String, String> resMap = new HashMap<>();

        for (Iterator<TaskRunning> ite = taskRunnings.iterator(); ite.hasNext(); ) {
            TaskRunning taskRunning = ite.next();
            Task task = taskRunning.getTask();
            File resultFile = taskRunning.getResult().getFile();
            String content = FileUtil.readFullFile(new java.io.File(resultFile.getPath() + resultFile.getFileName()));
            resMap.put("desc", content);
        }
        String json = JSONUtil.toJson(resMap);

//        String hardcodeJson = "{\"result\":[{\"desc\":\"abc\"}]}";

        return new ResponseEntity(json, HttpStatus.OK);
    }
}