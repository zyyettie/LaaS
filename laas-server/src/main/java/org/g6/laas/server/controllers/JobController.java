package org.g6.laas.server.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.AnalysisEngine;
import org.g6.laas.core.engine.task.AnalysisTask;
import org.g6.laas.core.engine.task.report.ReportBuilder;
import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.core.engine.task.report.template.TemplateViewResolver;
import org.g6.laas.server.database.entity.File;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.result.TaskResult;
import org.g6.laas.server.database.entity.task.Scenario;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.repository.IJobRepository;
import org.g6.laas.server.database.repository.IScenarioRepository;
import org.g6.laas.server.queue.JobHelper;
import org.g6.laas.server.queue.JobQueue;
import org.g6.laas.server.queue.QueueJob;
import org.g6.laas.server.queue.QueueTask;
import org.g6.laas.server.vo.FileInfo;
import org.g6.laas.server.vo.TaskRunningResult;
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;
import org.g6.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@Slf4j
public class JobController {
    @Autowired
    private IJobRepository jobRepo;
    @Autowired
    private IScenarioRepository scenarioRepo;
    @Autowired
    private JobHelper jobHelper;
    @Autowired
    private AnalysisEngine analysisEngine;
    @Autowired
    JobQueue queue;


    @RequestMapping(value = "/controllers/jobs/{jobId}")
    ResponseEntity<String> runJob(@PathVariable Long jobId) {
        Job job = jobRepo.findOne(jobId);
        JobRunning jobRunning = createRunningRecords4JobAndTask(job);

        JobRunningResult runningResult = runTasks(jobRunning);

        Map<String, String> jsonMap = new HashMap();
        jsonMap.put("job_id", String.valueOf(job.getId()));
        jsonMap.put("job_running_id", String.valueOf(jobRunning.getId()));
        jsonMap.put("is_syn", runningResult.isSyn() ? "true" : "false");

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
        JobRunning retJobRunning = jobHelper.saveJobRunning(jobRunning);

        return retJobRunning;
    }

    /**
     * Run all tasks
     *
     * @param jobRunning
     * @return 0: Synchronous 1: asynchronous
     */
    private JobRunningResult runTasks(JobRunning jobRunning) {
        Job job = jobRunning.getJob();
        List<String> strFiles = getLogFilesFromJob(job);
        Map<String, String> paramMap = JSONUtil.fromJson(job.getParameters());

        Collection<TaskRunning> taskRunnings = jobRunning.getTaskRunnings();
        QueueJob queueJob = new QueueJob();
        int failedTasks = 0, asynCount = 0;
        boolean isSyn = true;

        for (Iterator<TaskRunning> ite = taskRunnings.iterator(); ite.hasNext(); ) {
            TaskRunning taskRunning = ite.next();
            Task task = taskRunning.getTask();
            TaskRunningResult taskRunningResult = null;
            try {
                Object taskObj = getTaskObj(task, paramMap, strFiles);
                //taskObj is the instance of Task class which is used to run
                //task is the entity which contains different data loaded from database.
                log.debug("Start running task named " + task.getName());
                long timeStart = System.currentTimeMillis();
                taskRunningResult = runTask(taskObj, task);
                long duration = System.currentTimeMillis() - timeStart;
                log.debug("Finish running task named " + task.getName() + ". The duration is " + duration / 1000 + "s");
            } catch (Exception e) {
                failedTasks++;
                jobHelper.saveTaskRunningStatus(taskRunning, "FAILED");
                log.error("Exception is thrown while running task" + task.getName(), e);
            }
            if (taskRunningResult != null) {
                if (!taskRunningResult.isTimeout()) {
                    String report = jobHelper.genReport(taskRunningResult, task);
                    FileInfo resultFile = jobHelper.writeReportToFile(report);
                    jobHelper.handleResultFile(taskRunning, resultFile);

                    jobHelper.saveTaskRunningStatus(taskRunning, "SUCCESS");
                } else {
                    asynCount++;
                    if(!"N".equals(jobRunning.getSyn())){
                        jobRunning.setSyn("N");
                        jobHelper.saveJobRunning(jobRunning);
                    }
                    queueJob.addQueueTask(taskRunning, new QueueTask(taskRunningResult.getFuture()));
                    queueJob.setJobRunning(jobRunning);
                    queue.addJob(queueJob);
                    isSyn = false;
                }
            }
        }

        JobRunningResult jobRunningResult = new JobRunningResult();
        //Note the status of JobRunning is not required to change while moving to asynchronous mode
        if (isSyn) {
            jobRunning.setSyn("Y");
            if (failedTasks == 0) {
                //The status of JobRunning should be set to "SUCCESS" after all tasks are run successfully
                jobHelper.saveJobRunningStatus(jobRunning, "SUCCESS");
                jobRunningResult.setSuccess(true);
            } else if (failedTasks == taskRunnings.size()) {
                jobHelper.saveJobRunningStatus(jobRunning, "FAILED");
                jobRunningResult.setSuccess(false);
            } else {
                jobHelper.saveJobRunningStatus(jobRunning, "PARTIALLY SUCCESS");
                jobRunningResult.setSuccess(false);
            }
        }
        jobRunningResult.setSuccess(isSyn);

        return jobRunningResult;
    }

    /**
     * To run task, need to use reflection mechanism to get the task object according to the class name in Task entity
     *
     * @param task
     * @param paramMap
     * @param strFiles
     * @return
     * @throws Exception
     */
    private Object getTaskObj(Task task, Map<String, String> paramMap, List<String> strFiles) throws Exception {
        Class taskClass = Class.forName(task.getClassName());
        Object taskObj = taskClass.newInstance();
        Field[] fields = taskClass.getDeclaredFields();

        Method m1 = taskObj.getClass().getSuperclass().getDeclaredMethod("setFiles", List.class);
        m1.invoke(taskObj, strFiles);

        for (Map.Entry<String, String> entry : paramMap.entrySet()) {
            for (int i = 0; i < fields.length; i++) {
                boolean isAccessible = true;
                if (!fields[i].isAccessible()) {
                    isAccessible = false;
                    fields[i].setAccessible(true);
                }

                if (fields[i].getName().equals(entry.getKey())) {
                    ReflectUtil.set(fields[i], taskObj, entry.getValue());
                }
                if (!isAccessible) {
                    fields[i].setAccessible(false);
                }
            }
        }

        return taskObj;
    }

    private TaskRunningResult runTask(Object taskObj, Task task) throws ExecutionException, InterruptedException {
        Future future = analysisEngine.submit((AnalysisTask) taskObj);
        TaskRunningResult result = new TaskRunningResult();

        Object obj;
        try {
            obj = future.get(20000, TimeUnit.MILLISECONDS);
            result.setResult(obj);
            //TODO
            //throw new TimeoutException("Just for testing and remove this line later!");
        } catch (TimeoutException te) {
            log.info("The task named " + task.getName() + "is going in asynchronous running mode");
            result.setFuture(future);
            result.setTimeout(true);
            log.warn("Timeout while running task " + task.getName() + ", move to asynchronous mode");
        }

        return result;
    }

    private List<String> getLogFilesFromJob(Job job) {
        List<String> files = new ArrayList<>();
        for (Iterator<File> ite = job.getFiles().iterator(); ite.hasNext(); ) {
            File f = ite.next();
            files.add(f.getPath() + f.getFileName());
        }

        return files;
    }

    @Data
    class JobRunningResult {
        boolean syn;
        boolean success;
    }

}