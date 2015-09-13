package org.g6.laas.server.controllers;

import org.g6.laas.core.engine.AnalysisEngine;
import org.g6.laas.core.engine.task.AnalysisTask;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.server.database.entity.File;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.Scenario;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.repository.IJobRepository;
import org.g6.laas.server.database.repository.IJobRunningRepository;
import org.g6.laas.server.database.repository.IScenarioRepository;
import org.g6.laas.server.database.repository.ITaskRunningRepository;
import org.g6.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutionException;
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
    private IScenarioRepository scenarioRepo;

    @Autowired
    private AnalysisEngine analysisEngine;


    @RequestMapping(value = "/controllers/jobs/{jobId}")
    ResponseEntity<String> runJob(@PathVariable Long jobId) {
        prepareTestDate();

        Job job = jobRepo.findOne(jobId);
        JobRunning jobRunning = genRunningRecords4JobAndTask(job);

        runTasks(jobRunning);

        //TODO
        String json = genRetJSON(job);
        //returning JSON should contains jobID, Job running id, job status, task ids and corresponding status.
        return new ResponseEntity("{\"id\":" + job.getId() + "}", HttpStatus.OK);
    }

    private void prepareTestDate() {
        Job myJob = new Job();
        myJob.setName("Job" + Math.random());
        myJob.setParameters("{\"N\":\"60\", \"order\":\"desc\"}");

        Scenario _scenario = scenarioRepo.getOne(1l);
        Collection<Scenario> _scenarios = new ArrayList();
        _scenarios.add(_scenario);
        myJob.setScenarios(_scenarios);

        Job myRetJob = jobRepo.save(myJob);
        System.out.println(myRetJob.getId());

    }

    /**
     * Generate JobRunning and TaskRunning records.
     * The status of JobRunning is set to "running" before all tasks are completed
     *
     * @param job
     * @return JobRunning object for JobRunning
     */
    private JobRunning genRunningRecords4JobAndTask(Job job) {
        JobRunning jobRunning = new JobRunning();
        jobRunning.setJob(job);
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
        JobRunning retJobRunning = jobRunningRepo.save(jobRunning);

        return retJobRunning;
    }

    private void runTasks(JobRunning jobRunning) {
        Job job = jobRunning.getJob();
        List<String> strFiles = getLogFilesFromJob(job);
        Map<String, String> paramMap = JSONUtil.fromJson(job.getParameters());

        //TODO for testing hardcoded some data for log file
        strFiles.add("e:\\sm.log");

        Collection<TaskRunning> taskRunnings = jobRunning.getTaskRunnings();
        for (Iterator<TaskRunning> ite = taskRunnings.iterator(); ite.hasNext(); ) {
            TaskRunning taskRunning = ite.next();
            Task task = taskRunning.getTask();

            try {
                Object taskObj = getTaskObj(task, paramMap, strFiles);
                Object result = runTask(taskObj);

            } catch (Exception e) {
                saveTaskRunningStatus(taskRunning, "FAILED");
                saveJobRunningStatus(jobRunning, "FAILED");
                throw new LaaSRuntimeException("Exception is thrown while running task" + task.getName(), e);
            }
            //If the task runs successfully, its status should be set to "SUCCESS"
            saveTaskRunningStatus(taskRunning, "SUCCESS");
        }

        //The status of JobRunning should be set to "SUCCESS" after all tasked are run successfully
        saveJobRunningStatus(jobRunning, "SUCCESS");
    }

    private void saveTaskRunningStatus(TaskRunning taskRunning, String status){
        taskRunning.setStatus(status);
        taskRunningRepo.save(taskRunning);
    }

    private void saveJobRunningStatus(JobRunning jobRunning, String status){
        jobRunning.setStatus(status);
        jobRunningRepo.save(jobRunning);
    }

    /**
     * To run task, need to use reflection mechanism to get the task object according to the class name in Task entity
     * @param task
     * @param paramMap
     * @param strFiles
     * @return
     * @throws Exception
     */
    private Object getTaskObj(Task task, Map<String, String> paramMap, List<String> strFiles) throws Exception{
        Class taskClass = Class.forName(task.getClassName());
        Object taskObj = taskClass.newInstance();
        Field[] fields = taskClass.getDeclaredFields();

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            for (int i = 0; i < fields.length; i++) {
                boolean isAccessible = true;
                if (!fields[i].isAccessible()) {
                    isAccessible = false;
                    fields[i].setAccessible(true);
                }

                if (fields[i].getName().equals("files")) {
                    fields[i].set(taskObj, strFiles);
                } else if (fields[i].getName().equals(entry.getKey())) {
                    set(fields[i], taskObj, entry.getValue());
                }
                if (!isAccessible) {
                    fields[i].setAccessible(false);
                }
            }
        }

        return taskObj;
    }

    private Object runTask(Object taskObj) throws ExecutionException, InterruptedException {
        Future future = analysisEngine.submit((AnalysisTask) taskObj);
        Object obj = future.get();
        analysisEngine.shutdown();

        return obj;
    }

    private List<String> getLogFilesFromJob(Job job){
        List<String> strFiles = new ArrayList<>();
        for (Iterator<File> ite = job.getFiles().iterator(); ite.hasNext(); ) {
            File f = ite.next();
            strFiles.add(f.getPath() + f.getFileName());
        }

        return strFiles;
    }

    //TODO
    private String genRetJSON(Job job){

        return null;
    }

    //TODO move the method to ReflectUtil class
    private void set(Field field, Object obj, String value) throws IllegalAccessException {
        if (field.getType().getName().equals("int")) {
            field.setInt(obj, Integer.valueOf(value));
        } else if (field.getType().getName().equals("long")) {
            field.setLong(obj, Long.valueOf(value));
        } else if (field.getType().getName().equals("double")) {
            field.setDouble(obj, Double.valueOf(value));
        } else {
            field.set(obj, value);
        }
    }

}
