package org.g6.laas.server.controllers;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.*;
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
        Map<String, Object> jsonMap = new HashMap();
        Job job = jobService.findJobBy(jobId);
        List<String> rootCauses = validateFiles(job);

        if (!rootCauses.isEmpty()) {
            jsonMap.put("success", false);
            jsonMap.put("rootcauses", rootCauses);
        } else {
            JobRunning jobRunning = createRunningRecords4JobAndTask(job);
            JobService.JobRunningResult runningResult = jobService.runTasks(jobRunning);

            jsonMap.put("job_id", String.valueOf(job.getId()));
            jsonMap.put("job_running_id", String.valueOf(jobRunning.getId()));
            jsonMap.put("is_syn", runningResult.isSyn());
            jsonMap.put("success", runningResult.isSuccess());
            jsonMap.put("rootcauses", runningResult.getRootCauses());
        }

        return new ResponseEntity(JSONUtil.toJson(jsonMap), HttpStatus.OK);
    }

    private List<String> validateFiles(Job job) {
        List<File> strFiles = job.getFiles();
        List<String> rootCauses = new ArrayList();
        if(strFiles.isEmpty()){
            rootCauses.add("No files found for analysis");
            return rootCauses;
        }

        for (File file : strFiles) {
            if (!FileUtil.isFile(file.getPath() + file.getFileName())) {
                rootCauses.add(file.getOriginalName() + " isn't a valid file");
            }
        }
        return rootCauses;
    }

    /**
     * Insert JobRunning and ScenarioRunning records into database.
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

        List<Scenario> scenarios = job.getScenarios();

        for (Iterator<Scenario> ite = scenarios.iterator(); ite.hasNext(); ) {
            Scenario scenario = ite.next();
            ScenarioRunning scenarioRunning = new ScenarioRunning();
            scenarioRunning.setStatus("RUNNING");
            scenarioRunning.setScenario(scenario);
            scenarioRunning.setJobRunning(jobRunning);
            jobRunning.addScenarioRunning(scenarioRunning);
        }

        JobRunning retJobRunning = jobService.saveJobRunning(jobRunning);

        return retJobRunning;
    }

    private List<File> getFiles(Collection<File> files) {
        List<File> fileList = new ArrayList<>();
        for (Iterator<File> ite = files.iterator(); ite.hasNext(); ) {
            File file = ite.next();
            fileList.add(file);
        }
        return fileList;
    }

    @RequestMapping(value = "/controllers/jobRunnings/{id}/result")
    ResponseEntity<String> getJobRunningResult(@PathVariable Long id) {
        JobRunning jobRunning = jobService.findJobRunningBy(id);
        Collection<ScenarioRunning> taskRunnings = jobRunning.getScenarioRunnings();
        Map<String, String> resMap = new HashMap<>();

        for (Iterator<ScenarioRunning> ite = taskRunnings.iterator(); ite.hasNext(); ) {
            ScenarioRunning scenarioRunning = ite.next();
            File resultFile = scenarioRunning.getResult().getFile();
            String content = FileUtil.readFullFile(new java.io.File(resultFile.getPath() + resultFile.getFileName()));
            resMap.put("desc", content);
        }

        String json = JSONUtil.toJson(resMap);
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @RequestMapping(value = "/controllers/jobRunnings/{id}")
    ResponseEntity<String> reRunJobRunning(@PathVariable Long id) {
        JobRunning jobRunning = jobService.findJobRunningBy(id);

        JobRunning newJobRunning = new JobRunning();
        newJobRunning.setJob(jobRunning.getJob());
        newJobRunning.setParameters(jobRunning.getParameters());
        newJobRunning.setFiles(getFiles(jobRunning.getFiles()));
        newJobRunning.setStatus("RUNNING");

        for (Iterator<ScenarioRunning> ite = jobRunning.getScenarioRunnings().iterator(); ite.hasNext(); ) {
            ScenarioRunning scenarioRunning = ite.next();
            ScenarioRunning newScenarioRunning = new ScenarioRunning();

            newScenarioRunning.setStatus("RUNNING");
            newScenarioRunning.setJobRunning(newJobRunning);
            newScenarioRunning.setScenario(scenarioRunning.getScenario());

            newJobRunning.addScenarioRunning(newScenarioRunning);
        }


        JobRunning retJobRunning = jobService.saveJobRunning(newJobRunning);
        JobService.JobRunningResult runningResult = jobService.runTasks(retJobRunning);

        Map<String, Object> jsonMap = new HashMap();
        jsonMap.put("job_id", String.valueOf(retJobRunning.getJob().getId()));
        jsonMap.put("job_running_id", String.valueOf(retJobRunning.getId()));
        jsonMap.put("is_syn", runningResult.isSyn());
        jsonMap.put("success", runningResult.isSuccess());
        jsonMap.put("rootcauses", runningResult.getRootCauses());

        return new ResponseEntity(JSONUtil.toJson(jsonMap), HttpStatus.OK);
    }
}