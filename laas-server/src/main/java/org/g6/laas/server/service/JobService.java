package org.g6.laas.server.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.AnalysisEngine;
import org.g6.laas.core.engine.task.AnalysisTask;
import org.g6.laas.core.engine.task.report.ReportBuilder;
import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.result.TaskResult;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.repository.IJobRepository;
import org.g6.laas.server.database.repository.IJobRunningRepository;
import org.g6.laas.server.database.repository.ITaskRunningRepository;
import org.g6.laas.server.queue.JobQueue;
import org.g6.laas.server.queue.QueueJob;
import org.g6.laas.server.queue.QueueTask;
import org.g6.laas.server.vo.FileInfo;
import org.g6.laas.server.vo.TaskRunningResult;
import org.g6.util.ExceptionUtils;
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;
import org.g6.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Service
public class JobService {
    @Autowired
    private AnalysisEngine analysisEngine;
    @Autowired
    private IJobRepository jobRepo;
    @Autowired
    private IJobRunningRepository jobRunningRepo;
    @Autowired
    private ITaskRunningRepository taskRunningRepo;
    @Autowired
    JobQueue queue;

    public Job findJobBy(Long id) {
        return jobRepo.findOne(id);
    }

    public JobRunning findJobRunningBy(Long id) {
        return jobRunningRepo.findOne(id);
    }

    public void saveTaskRunningStatus(TaskRunning taskRunning, String status) {
        taskRunning.setStatus(status);
        taskRunningRepo.save(taskRunning);
    }

    public void saveJobRunningStatus(JobRunning jobRunning, String status) {
        jobRunning.setStatus(status);
        jobRunningRepo.save(jobRunning);
    }

    public JobRunning saveJobRunning(JobRunning jobRunning) {
        return jobRunningRepo.save(jobRunning);
    }

    public TaskRunning saveTaskRunning(TaskRunning taskRunning) {
        return taskRunningRepo.save(taskRunning);
    }

    public List<JobRunning> findUnFinishedJobInQueue(String syn, String status) {
        return jobRunningRepo.findUnFinishedJobInQueue(syn, status);
    }

    public String genReport(TaskRunningResult taskRunningResult, Task task, boolean isReport) {
        if(!isReport){
           return taskRunningResult.getResult().toString();
        }

        ReportModel model = new ReportModel();
        model.setAttribute("task_running_result", taskRunningResult.getResult());
        ReportBuilder builder = new ReportBuilder();
        String report = builder.build(model, task.getClassName());

        return report;
    }

    public FileInfo writeReportToFile(String report) {
        String path = FileUtil.getvalue("result_file_full_path", "sm.properties");
        //TODO NOTE to avoid concurrent operation, should add login name in the path.
        String fileName = System.currentTimeMillis() + ".laas";
        FileUtil.writeFile(report, path + fileName);

        return new FileInfo(path, fileName);
    }

    public void handleResultFile(TaskRunning taskRunning, FileInfo info) {
        File f = new File();
        f.setPath(info.getPath());
        f.setFileName(info.getName());
        f.setOriginalName(info.getName());

        TaskResult taskResult = new TaskResult();
        taskResult.setFile(f);
        taskRunning.setResult(taskResult);
    }

    /**
     * Run all tasks
     *
     * @param jobRunning
     * @return 0: Synchronous 1: asynchronous
     */
    public JobRunningResult runTasks(JobRunning jobRunning) {
        Job job = jobRunning.getJob();
        JobRunningResult jobRunningResult = new JobRunningResult();
        List<String> strFiles = getLogFilesFromJob(jobRunning);
        boolean isValidFile = true;
        for(String file : strFiles){
            if(!FileUtil.isFile(file)){
                jobRunningResult.getRootCauses().add(file + " isn't a valid file");
                jobRunningResult.setSuccess(false);
                isValidFile = false;
            }
        }

        if(!isValidFile)
            return jobRunningResult;

        Map<String, String> paramMap = JSONUtil.fromJson(job.getParameters());

        Collection<TaskRunning> taskRunnings = jobRunning.getTaskRunnings();
        QueueJob queueJob = new QueueJob();
        int failedTasks = 0, asynCount = 0;
        boolean isSyn = true;

        for (Iterator<TaskRunning> ite = taskRunnings.iterator(); ite.hasNext(); ) {
            TaskRunning taskRunning = ite.next();
            Task task = taskRunning.getTask();
            TaskRunningResult taskRunningResult = null;
            ReflectionObjWrapper taskWrapper = null;
            try {
                taskWrapper = getTaskObj(task, paramMap, strFiles);

                //taskObj is the instance of Task class which is used to run
                //task is the entity which contains different data loaded from database.
                log.debug("Start running task named " + task.getName());
                long timeStart = System.currentTimeMillis();
                taskRunningResult = runTask(taskWrapper, task);
                long duration = System.currentTimeMillis() - timeStart;
                log.debug("Finish running task named " + task.getName() + ". The duration is " + duration / 1000 + "s");
            } catch (Exception e) {
                failedTasks++;
                saveTaskRunningStatus(taskRunning, "FAILED");
                String rootCause = ExceptionUtils.getRootCauseMessage(e);
                jobRunningResult.rootCauses.add(rootCause);
                log.error("Exception is thrown while running task" + task.getName(), e);
            }
            if (taskRunningResult != null) {
                if (!taskRunningResult.isTimeout()) {
                    String report = genReport(taskRunningResult, task, taskWrapper != null ? taskWrapper.isReport() : true);
                    FileInfo resultFile = writeReportToFile(report);
                    handleResultFile(taskRunning, resultFile);

                    saveTaskRunningStatus(taskRunning, "SUCCESS");
                } else {
                    asynCount++;
                    if (!"N".equals(jobRunning.getSyn())) {
                        jobRunning.setSyn("N");
                        jobRunning.getToUsers().clear();
                        jobRunning.addUser(jobRunning.getCreatedBy());
                        String summary = "Your job named " + job.getName() + " is running in the background, please check your inbox later";
                        jobRunning.setSummary(summary);
                        saveJobRunning(jobRunning);
                    }
                    queueJob.addQueueTask(taskRunning, new QueueTask(taskRunningResult.getFuture(), taskRunningResult.isReport()));
                    queueJob.setJobRunning(jobRunning);
                    queue.addJob(queueJob);
                    isSyn = false;
                }
            }
        }

        if (!jobRunningResult.getRootCauses().isEmpty()) {
            jobRunningResult.setSuccess(false);
            return jobRunningResult;
        }

        //Note the status of JobRunning is not required to change while moving to asynchronous mode
        if (isSyn) {
            jobRunning.setSyn("Y");
            if (failedTasks == 0) {
                //The status of JobRunning should be set to "SUCCESS" after all tasks are run successfully
                saveJobRunningStatus(jobRunning, "SUCCESS");
                jobRunningResult.setSuccess(true);
            } else if (failedTasks == taskRunnings.size()) {
                saveJobRunningStatus(jobRunning, "FAILED");
                jobRunningResult.setSuccess(false);
            } else {
                saveJobRunningStatus(jobRunning, "PARTIALLY SUCCESS");
                jobRunningResult.setSuccess(false);
            }
        }else{
            jobRunningResult.setSuccess(true);
        }
        jobRunningResult.setSyn(isSyn);

        return jobRunningResult;
    }


    /**
     * To run task, need to use reflection mechanism to get the task object
     * according to the class name in Task entity
     *
     * @param task
     * @param paramMap
     * @param strFiles
     * @return
     * @throws Exception
     */
    private ReflectionObjWrapper getTaskObj(Task task, Map<String, String> paramMap, List<String> strFiles) throws Exception {
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

        Method m2 = taskObj.getClass().getMethod("isReport");
        boolean isReport = (Boolean)m2.invoke(taskObj);

        return new ReflectionObjWrapper(isReport, taskObj);
    }

    private TaskRunningResult runTask(ReflectionObjWrapper taskWrapper, Task task) throws ExecutionException, InterruptedException {
        Future future = analysisEngine.submit((AnalysisTask) taskWrapper.getObj());
        TaskRunningResult result = new TaskRunningResult();
        result.setReport(taskWrapper.isReport());

        Object obj;
        try {
            obj = future.get(20000, TimeUnit.MILLISECONDS);
            result.setResult(obj);
        } catch (TimeoutException te) {
            log.info("The task named " + task.getName() + "is going in asynchronous running mode");
            result.setFuture(future);
            result.setTimeout(true);
            log.warn("Timeout while running task " + task.getName() + ", move to asynchronous mode");
        }

        return result;
    }

    private List<String> getLogFilesFromJob(JobRunning jobRunning) {
        List<String> files = new ArrayList<>();
        for (Iterator<File> ite = jobRunning.getFiles().iterator(); ite.hasNext(); ) {
            File f = ite.next();
            files.add(f.getPath() + f.getFileName());
        }

        return files;
    }

    @Data
    public class JobRunningResult {
        boolean syn;
        boolean success;
        List<String> rootCauses = new ArrayList<>();
    }

    @Data
    @AllArgsConstructor
    public class ReflectionObjWrapper {
        boolean report;
        Object obj;
    }

}