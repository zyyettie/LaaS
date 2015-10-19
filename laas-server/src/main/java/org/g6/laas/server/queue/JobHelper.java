package org.g6.laas.server.queue;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.g6.laas.core.engine.task.report.ReportBuilder;
import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.server.database.entity.JobRunning;
import org.g6.laas.server.database.entity.task.Task;
import org.g6.laas.server.database.entity.task.TaskRunning;
import org.g6.laas.server.database.repository.IJobRunningRepository;
import org.g6.laas.server.database.repository.ITaskRunningRepository;
import org.g6.laas.server.vo.FileInfo;
import org.g6.laas.server.vo.TaskRunningResult;
import org.g6.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

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

    public JobRunning saveJobRunning(JobRunning jobRunning) {
        return jobRunningRepo.save(jobRunning);
    }

    public TaskRunning saveTaskRunning(TaskRunning taskRunning) {
        return taskRunningRepo.save(taskRunning);
    }


    public String genReport(TaskRunningResult taskRunningResult, Task task) {
        ReportModel model = new ReportModel();
        model.setAttribute("task_running_result", taskRunningResult.getResult());
        ReportBuilder builder = new ReportBuilder();
        String report = builder.build(model, task.getClassName());

        return report;
    }

    public FileInfo writeReportToFile(String report) {
        String path = FileUtil.getvalue("result_file_full_path", "sm.properties");
        //TODO NOTE to avoid concurrent operation, should add login name in the path.
        String fileName = System.currentTimeMillis() + ".log";
        FileUtil.writeFile(report, path + fileName);

        return new FileInfo(path, fileName);
    }


}
