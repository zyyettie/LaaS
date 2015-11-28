package org.g6.laas.server.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.AnalysisEngine;
import org.g6.laas.core.engine.task.AnalysisTask;
import org.g6.laas.core.engine.task.ChainTask;
import org.g6.laas.core.engine.task.TaskChain;
import org.g6.laas.core.engine.task.report.ReportBuilder;
import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.server.database.entity.file.File;
import org.g6.laas.server.database.entity.Job;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.result.ScenarioResult;
import org.g6.laas.server.database.entity.task.OrderedTask;
import org.g6.laas.server.database.entity.task.ScenarioRunning;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.repository.IJobRepository;
import org.g6.laas.server.database.repository.IJobRunningRepository;
import org.g6.laas.server.database.repository.IScenarioRunningRepository;
import org.g6.laas.server.queue.JobQueue;
import org.g6.laas.server.queue.QueueJob;
import org.g6.laas.server.queue.QueueScenario;
import org.g6.laas.server.vo.FileInfo;
import org.g6.laas.server.vo.ScenarioRunningResult;
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
    private IScenarioRunningRepository scenarioRunningRepo;
    @Autowired
    JobQueue queue;

    public Job findJobBy(Long id) {
        return jobRepo.findOne(id);
    }

    public JobRunning findJobRunningBy(Long id) {
        return jobRunningRepo.findOne(id);
    }

    public void saveScenarioRunningStatus(ScenarioRunning scenarioRunning, String status) {
        scenarioRunning.setStatus(status);
        scenarioRunningRepo.save(scenarioRunning);
    }

    public void saveJobRunningStatus(JobRunning jobRunning, String status) {
        jobRunning.setStatus(status);
        jobRunningRepo.save(jobRunning);
    }

    public JobRunning saveJobRunning(JobRunning jobRunning) {
        return jobRunningRepo.save(jobRunning);
    }

    public ScenarioRunning saveScenarioRunning(ScenarioRunning scenarioRunning) {
        return scenarioRunningRepo.save(scenarioRunning);
    }

    public List<JobRunning> findUnFinishedJobInQueue(String syn, String status) {
        return jobRunningRepo.findUnFinishedJobInQueue(syn, status);
    }

    public String genReport(ScenarioRunningResult result) {
        if (!result.isReport()) {
            return result.getResult().toString();
        }

        ReportModel model = new ReportModel();
        model.setAttribute("task_running_result", result.getResult());
        ReportBuilder builder = new ReportBuilder();
        String report = builder.build(model, result.getReportTemplate());

        return report;
    }

    public FileInfo writeReportToFile(String report) {
        String path = FileUtil.getvalue("result_file_full_path", "sm.properties");
        //TODO NOTE to avoid concurrent operation, should add login name in the path.
        String fileName = System.currentTimeMillis() + ".laas";
        FileUtil.writeFile(report, path + fileName);

        return new FileInfo(path, fileName);
    }

    public void handleResultFile(ScenarioRunning scenarioRunning, FileInfo info) {
        File f = new File();
        f.setPath(info.getPath());
        f.setFileName(info.getName());
        f.setOriginalName(info.getName());

        ScenarioResult scenarioResult = new ScenarioResult();
        scenarioResult.setFile(f);
        scenarioRunning.setResult(scenarioResult);
    }

    /**
     * Run all tasks
     *
     * @param jobRunning
     * @return 0: Synchronous 1: asynchronous
     */
    public JobRunningResult runJob(JobRunning jobRunning) {
        Job job = jobRunning.getJob();
        JobRunningResult jobRunningResult = new JobRunningResult();
        List<LogFile> logFiles = getLogFilesFromJob(jobRunning);
        Map<String, Object> paramMap = JSONUtil.fromJson(job.getParameters());
        paramMap.put("files", logFiles);

        Collection<ScenarioRunning> scenarioRunnings = jobRunning.getScenarioRunnings();
        boolean isSyn = true;

        for (Iterator<ScenarioRunning> ite = scenarioRunnings.iterator(); ite.hasNext(); ) {
            ScenarioRunning scenarioRunning = ite.next();
            List<OrderedTask> orderedTasks = scenarioRunning.getScenario().getOrderedTasks();
            ScenarioRunningResult scenarioRunningResult;
            Task task;

            if (orderedTasks.size() == 1) {
                //if there is only one Task under a workflow, just to run this task directly
                task = orderedTasks.get(0).getTask();
                scenarioRunningResult = runSingleTask(scenarioRunning, task, paramMap, jobRunningResult);
            } else {
                scenarioRunningResult = runWorkflow(scenarioRunning, orderedTasks, paramMap, jobRunningResult);
            }

            isSyn = handleScenarioRunning(scenarioRunningResult, scenarioRunning, jobRunning);
        }

        if (!jobRunningResult.getRootCauses().isEmpty()) {
            jobRunningResult.setSuccess(false);
            return jobRunningResult;
        }

        handleJobRunning(isSyn, jobRunning, jobRunningResult);

        return jobRunningResult;
    }

    private void handleJobRunning(boolean isSyn, JobRunning jobRunning, JobRunningResult jobRunningResult){
        //Note the status of JobRunning is not required to change while moving to asynchronous mode
        if (isSyn) {
            jobRunning.setSyn("Y");
            if(jobRunningResult.getRootCauses().isEmpty()){
                saveJobRunningStatus(jobRunning, "SUCCESS");
                jobRunningResult.setSuccess(true);
            } else{
                saveJobRunningStatus(jobRunning, "FAILED");
                jobRunningResult.setSuccess(false);
            }
        } else {
            jobRunningResult.setSuccess(true);
        }
        jobRunningResult.setSyn(isSyn);
    }

    private boolean handleScenarioRunning(ScenarioRunningResult result, ScenarioRunning scenarioRunning, JobRunning jobRunning){
        boolean isSyn = true;
        if (result != null) {
            QueueJob queueJob = new QueueJob();
            if (!result.isTimeout()) {
                String report = genReport(result);
                FileInfo resultFile = writeReportToFile(report);
                handleResultFile(scenarioRunning, resultFile);

                saveScenarioRunningStatus(scenarioRunning, "SUCCESS");
            } else {
                if (!"N".equals(jobRunning.getSyn())) {
                    jobRunning.setSyn("N");
                    jobRunning.getToUsers().clear();
                    jobRunning.addUser(jobRunning.getCreatedBy());
                    String summary = "Your job named " + jobRunning.getJob().getName() + " is running in the background, please check your inbox later";
                    jobRunning.setSummary(summary);
                    saveJobRunning(jobRunning);
                }
                QueueScenario queueScenario = new QueueScenario(result.getFuture(), result.isReport(), result.getReportTemplate());
                queueJob.addQueueScenario(scenarioRunning, queueScenario);
                queueJob.setJobRunning(jobRunning);
                queue.addJob(queueJob);
                isSyn = false;
            }
        }

        return isSyn;
    }

    private ScenarioRunningResult runSingleTask(ScenarioRunning scenarioRunning, Task task, Map<String, Object> paramMap, JobRunningResult jobRunningResult) {
        ScenarioRunningResult result = null;
        ReflectionObjWrapper taskWrapper;
        try {
            taskWrapper = getTaskObj(task, paramMap);

            //taskObj is the instance of Task class which is used to run
            //task is the entity which contains different data loaded from database.
            log.debug("Start running task named " + task.getName());
            long timeStart = System.currentTimeMillis();
            result = runTask(taskWrapper, task);
            long duration = System.currentTimeMillis() - timeStart;
            log.debug("Finish running task named " + task.getName() + ". The duration is " + duration / 1000 + "s");
        } catch (Exception e) {
            saveScenarioRunningStatus(scenarioRunning, "FAILED");
            String rootCause = ExceptionUtils.getRootCauseMessage(e);
            jobRunningResult.rootCauses.add(rootCause);
            jobRunningResult.setSuccess(false);
            log.error("Exception is thrown while running task" + task.getName(), e);
        }

        return result;
    }

    private ScenarioRunningResult runWorkflow(ScenarioRunning scenarioRunning, List<OrderedTask> orderedTasks, Map paramMap, JobRunningResult jobRunningResult) {
        Collections.sort(orderedTasks);

        TaskChain taskChain = new TaskChain();
        taskChain.setParamMap(paramMap);
        String lastTaskClassName = null;
        Future future = null;
        ScenarioRunningResult result = new ScenarioRunningResult();
        String scenarioName = scenarioRunning.getScenario().getName();
        try {
            for (OrderedTask orderedTask : orderedTasks) {
                Task task = orderedTask.getTask();
                String taskClassName = task.getClassName();
                Object taskObj = ReflectUtil.newInstance(taskClassName);
                taskChain.addTask((ChainTask) taskObj);

                lastTaskClassName = task.getClassName();
            }

            result.setReportTemplate(lastTaskClassName);
            future = analysisEngine.submit(taskChain);
            Object obj = future.get(20000, TimeUnit.MILLISECONDS);
            // The type of obj is Map and two elements are included in it.
            // one is isReport and another is result that is used to generate report.
            Map<String, Object> resultMap = (Map<String, Object>) obj;
            result.setResult(resultMap.get("result"));
            result.setReport(resultMap.get("isReport") == null ? true : Boolean.parseBoolean(resultMap.get("isReport").toString()));
        } catch (TimeoutException te) {
            log.info("The execution on scenario named " + scenarioName + "is going in asynchronous running mode due to timeout");
            result.setFuture(future);
            result.setTimeout(true);
        } catch (Exception e) {
            saveScenarioRunningStatus(scenarioRunning, "FAILED");
            String rootCause = ExceptionUtils.getRootCauseMessage(e);
            jobRunningResult.rootCauses.add(rootCause);
            jobRunningResult.setSuccess(false);
            log.error("Exception happens while running task workflow for scenario named " + scenarioName, e);
        }

        return result;
    }

    /**
     * To run task, need to use reflection mechanism to get the task object
     * according to the class name in Task entity
     *
     * @param task
     * @param paramMap
     * @return
     * @throws Exception
     */
    private ReflectionObjWrapper getTaskObj(Task task, Map<String, Object> paramMap) throws Exception {
        Class taskClass = Class.forName(task.getClassName());
        Object taskObj = taskClass.newInstance();
        Field[] fields = taskClass.getDeclaredFields();


        Method m1 = taskObj.getClass().getSuperclass().getDeclaredMethod("setFiles", List.class);
        List<LogFile> selectFiles = (List<LogFile>) paramMap.get("files");
        m1.invoke(taskObj, selectFiles);

        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
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
        boolean isReport = (Boolean) m2.invoke(taskObj);

        return new ReflectionObjWrapper(isReport, taskObj);
    }

    private ScenarioRunningResult runTask(ReflectionObjWrapper taskWrapper, Task task) throws ExecutionException, InterruptedException {
        Future future = analysisEngine.submit((AnalysisTask) taskWrapper.getObj());
        ScenarioRunningResult result = new ScenarioRunningResult();
        result.setReport(taskWrapper.isReport());
        result.setReportTemplate(task.getClassName());

        try {
            Object obj = future.get(20000, TimeUnit.MILLISECONDS);
            result.setResult(obj);
            throw new TimeoutException("aaa");
        } catch (TimeoutException te) {
            log.info("The task named " + task.getName() + "is going in asynchronous running mode");
            result.setFuture(future);
            result.setTimeout(true);
            log.warn("Timeout while running task " + task.getName() + ", move to asynchronous mode");
        }

        return result;
    }

    private List<LogFile> getLogFilesFromJob(JobRunning jobRunning) {
        List<LogFile> files = new ArrayList<>();
        for (Iterator<File> ite = jobRunning.getFiles().iterator(); ite.hasNext(); ) {
            File f = ite.next();
            files.add(new LogFile(f.getPath() + f.getFileName(), f.getOriginalName()));
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