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
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;
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
        //prepareTestData();

        Job job = jobRepo.findOne(jobId);
        JobRunning jobRunning = createRunningRecords4JobAndTask(job);

        boolean isSyn = runTasks(jobRunning);

        Map<String, String> jsonMap = new HashMap();
        jsonMap.put("job_id", String.valueOf(job.getId()));
        jsonMap.put("job_running_id", String.valueOf(jobRunning.getId()));
        jsonMap.put("is_syn", isSyn ? "true" : "false");

        return new ResponseEntity(JSONUtil.toJson(jsonMap), HttpStatus.OK);
    }

    /**
     * Generate JobRunning and TaskRunning records.
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
    private boolean runTasks(JobRunning jobRunning) {
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
                taskRunningResult = runTask(taskObj, task);
            } catch (Exception e) {
                failedTasks++;
                jobHelper.saveTaskRunningStatus(taskRunning, "FAILED");
                log.error("Exception is thrown while running task" + task.getName(), e);
            }
            if (taskRunningResult != null) {
                if (!taskRunningResult.isTimeout) {
                    String report = genReport(taskRunningResult, task);
                    FileInfo resultFile = writeReportToFile(report);
                    File f = new File();
                    f.setPath(resultFile.getPath());
                    f.setFileName(resultFile.getName());
                    f.setOriginalName(resultFile.getName());

                    TaskResult taskResult = new TaskResult();
                    Collection<File> files = new ArrayList();
                    files.add(f);
                    taskResult.setFiles(files);
                    taskRunning.setResult(taskResult);

                    jobHelper.saveTaskRunningStatus(taskRunning, "SUCCESS");


                } else {
                    asynCount++;
                    queueJob.addQueueTask(taskRunning, new QueueTask(taskRunningResult.getFuture()));
                    queueJob.setJobRunning(jobRunning);
                    queue.addJob(queueJob);
                    isSyn = false;
                }
            }
        }

        //Note the status of JobRunning is not required to changed while moving to asynchronous mode
        if (isSyn) {
            if (failedTasks == 0) {
                //The status of JobRunning should be set to "SUCCESS" after all tasked are run successfully
                jobHelper.saveJobRunningStatus(jobRunning, "SUCCESS");
            } else if (failedTasks == taskRunnings.size()) {
                jobHelper.saveJobRunningStatus(jobRunning, "FAILED");
            } else {
                jobHelper.saveJobRunningStatus(jobRunning, "PARTIALLY SUCCESS");
            }
        }

        return isSyn ? true : false;
    }

    private String genReport(TaskRunningResult taskRunningResult, Task task) {
        //java.io.File handlebarsTemplate = FileUtil.getFile("report/template/" + task.getClassName());

        ReportModel model = new ReportModel();
        model.setAttribute("task_running_result", taskRunningResult.getResult());


        TemplateViewResolver resolver = new TemplateViewResolver();
        ReportBuilder builder = new ReportBuilder(resolver);
        String report = builder.build(model, "/report/template/" + task.getClassName());

        return report;
    }

    private FileInfo writeReportToFile(String report) {
        String path = FileUtil.getvalue("result_file_full_path", "sm.properties");
        //TODO NOTE to avoid concurrent operation, should add login name in the path.
        String fileName = System.currentTimeMillis() + ".log";
        FileUtil.writeFile(report, path + fileName);

        return new FileInfo(path, fileName);
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
                    set(fields[i], taskObj, entry.getValue());
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
            result.setFuture(future);
            result.setTimeout(true);
            log.warn("Timeout while running task " + task.getName() + ", move to asynchronous mode");
        }

        return result;
    }

    private List<String> getLogFilesFromJob(Job job) {
        List<String> strFiles = new ArrayList<>();
        for (Iterator<File> ite = job.getFiles().iterator(); ite.hasNext(); ) {
            File f = ite.next();
            strFiles.add(f.getPath() + f.getFileName());
        }

        return strFiles;
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

    @Data
    class TaskRunningResult {
        Future future;
        boolean isTimeout = false;
        Object result;
    }

    @Data
    @AllArgsConstructor
    class FileInfo {
        String path;
        String name;
    }

}
