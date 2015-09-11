package org.g6.laas.server.controllers;

import org.g6.laas.core.engine.AnalysisEngine;
import org.g6.laas.core.engine.task.AnalysisTask;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.result.TaskResult;
import org.g6.laas.server.database.entity.task.Scenario;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.repository.IJobRepository;
import org.g6.laas.server.database.repository.IJobRunningRepository;
import org.g6.laas.server.database.repository.ITaskRepository;
import org.g6.laas.server.database.repository.ITaskRunningRepository;
import org.g6.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
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
    private ITaskRunningRepository taskRunningRepo;

    @Autowired
    private AnalysisEngine analysisEngine;

    @RequestMapping(value = "/controllers/jobs")
    ResponseEntity<String> saveJob(@RequestBody Job job) {
        return new ResponseEntity("{\"id\":" + job.getId() + "}", HttpStatus.OK);
    }

    @RequestMapping(value = "/controllers/jobs/{jobId}")
    ResponseEntity<String> runJob(@PathVariable Long jobId) {

        Job job = jobRepo.findOne(jobId);

        JobRunning jobRunning = new JobRunning();
        jobRunning.setJob(job);
        jobRunning.setStatus("running");

        Collection<Scenario> scenarios = job.getScenarios();

        Collection<TaskRunning> taskRunnings = new ArrayList<>();

        Collection<Task> tasks;

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
                Field[] fields = taskClass.getDeclaredFields();

                Map<String, String> paramMap = JSONUtil.fromJson(jobRunning.getJob().getParameters());

                for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                    for(int i=0; i < fields.length; i++){
                        if(fields[i].getName().equals(entry.getKey())){
                            fields[i].set(taskObj, entry.getValue());
                        }
                    }
                }

                Future future = analysisEngine.submit((AnalysisTask) taskObj);
                // how to handle the result, save it in database or something else?
                Object retObj = future.get();

                //here update task status
                taskRunning.setStatus("SUCCESS");
                taskRunningRepo.save(taskRunning);
            } catch (Exception e) {
                jobRunning.setStatus("FAILED");
                throw new LaaSRuntimeException("exception is thrown while runing tasks", e);
            }
        }
        jobRunning.setStatus("SUCCESS");
        jobRunningRepo.save(jobRunning);
    }


    private void runTask() {

    }
}
