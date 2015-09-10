package org.g6.laas.server.controllers;

import org.g6.laas.core.engine.AnalysisEngine;
import org.g6.laas.core.engine.task.AnalysisTask;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.Scenario;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.repository.IJobRepository;
import org.g6.laas.server.database.repository.IJobRunningRepository;
import org.g6.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;

@RestController
public class JobController {

    @Autowired
    private IJobRepository jobRepo;
    @Autowired
    private IJobRunningRepository jobRunningRepo;

    @Autowired
    private AnalysisEngine analysisEngine;

    @RequestMapping(value = "/controllers/jobs")
    ResponseEntity<String> runJob(Long jobId) {

        Job job = jobRepo.findOne(jobId);

        JobRunning jobRunning = new JobRunning();
        jobRunning.setJob(job);
        jobRunning.setStatus("running");

        Collection<Scenario> scenarios = job.getScenarios();

        Collection<TaskRunning> taskRunnings = new ArrayList<>();

        Collection<Task> tasks = null;

        for (Iterator<Scenario> it = scenarios.iterator(); it.hasNext(); ) {
            Scenario scenario = it.next();
            tasks = scenario.getTasks();
            for (Iterator<Task> ite = tasks.iterator(); ite.hasNext(); ) {
                Task task = ite.next();
                TaskRunning taskRunning = new TaskRunning();
                taskRunning.setTask(task);
                taskRunning.setJobRunning(jobRunning);
                taskRunnings.add(taskRunning);
            }
        }
        jobRunning.setTaskRunnings(taskRunnings);
        JobRunning retJobRunning = jobRunningRepo.save(jobRunning);

        //here should pick up all tasks from returning JobRunning object
        //after running all tasks, need to run


        return new ResponseEntity("{\"id\":" + job.getId() + "}", HttpStatus.OK);
    }

    private void runTasks(JobRunning jobRunning) {
        Collection<TaskRunning> taskRunnings = jobRunning.getTaskRunnings();
        for (Iterator<TaskRunning> ite = taskRunnings.iterator(); ite.hasNext(); ) {
            TaskRunning taskRunning = ite.next();
            Task task = taskRunning.getTask();
            String className = task.getClassName();
            Class taskClass;
            try {
                taskClass = Class.forName(className);
                Object taskObj = taskClass.newInstance();

                Map<String, String> paramMap = JSONUtil.fromJson(jobRunning.getJob().getParameters());

                //set values to all the fields used to run task

                Future future = analysisEngine.submit((AnalysisTask) taskObj);
                Object retObj = future.get();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private void runTask(){

    }
}
