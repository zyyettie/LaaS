package org.g6.laas.sm.task;

import lombok.Data;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.result.SplitResult;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.TrueRule;
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.util.CompressionUtil;
import org.g6.util.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * There are different process id and thread id in SM RTE logfile
 * This task is to separate all the records into different log files per process id and thread id
 * the separation rule is there should be different folders matching process id. Under different folders for
 * process there should be different folders form thread id where the corresponding log files are there
 *
 * @author Johnson Jiang
 * @version 1.0
 */
@Data
public class SplitProcessAndThreadTask extends SMRTETask<String> {
    private Map<String, ProcessIdHelper> splitMap;
    private int processId;
    private int threadId;

    /**
     * return the result
     *
     * @return a link to the zip file which contains all the log files separated
     */
    @Override
    protected String process() {
        String tempRootPath = FileUtil.getValue("zip_file_temp_path", "/server.properties");
        long fileNum = System.currentTimeMillis();
        String fileName = fileNum + ".zip";
        String zipFile = FileUtil.getValue("result_file_full_path", "/server.properties") + fileName;
        FileUtil.deleteDir(tempRootPath);
        FileUtil.createDir(tempRootPath);

        for (Map.Entry<String, ProcessIdHelper> entry : splitMap.entrySet()) {
            String processId = entry.getKey();
            ProcessIdHelper processIdHelper = entry.getValue();

            String processPath = tempRootPath + processId + File.separator;
            boolean flag = FileUtil.createDir(processPath);

            List<ThreadIdHelper> list = processIdHelper.getThreadIdHelperList();
            for (ThreadIdHelper threadIdHelper : list) {
                FileUtil.writeFile(threadIdHelper.getLineContentList(), processPath + threadIdHelper.getThreadId() + ".log");
            }
        }

        CompressionUtil.compress(tempRootPath, zipFile);
        return fileNum + "__zip";
    }

    @Override
    void initRule() {
        splitMap = new HashMap<>();
        String pattern = null;
        if (processId > 0 && threadId > 0) {
            pattern = "^\\s*(" + processId + ")\\(\\s+(" + threadId + ")\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+).+";
        } else if (processId > 0) {
            pattern = "^\\s*(" + processId + ")\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+).+";
        } else if (threadId > 0) {
            pattern = "^\\s*(\\d+)\\(\\s+(" + threadId + ")\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+).+";
        }
        Rule rule = new TrueRule();
        if(pattern != null){
            rule = rule.and(new RegexRule(pattern));
        }
        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                SplitResult result = line.split();
                handleLine(splitMap, result, line);
            }
        });
        addRule(rule);
    }

    private void handleLine(Map<String, ProcessIdHelper> splitMap, SplitResult result, Line line) {
        //NOTE: mostly this happens while no matching regex can be used to split a line
        if (result == null) return;
        String processId = String.valueOf((Integer) result.get("process_id").getValue());
        String threadId = String.valueOf((Integer) result.get("thread_id").getValue());

        if (splitMap.isEmpty()) {
            //NO process id in map, need to add one and the related data such as thread id, line.
            addProcessIdInMap(processId, threadId, line);
        } else {
            boolean hasProcessId = false;
            for (Map.Entry<String, ProcessIdHelper> entry : splitMap.entrySet()) {
                //start seeking if the same process id exists in map, if so, the next step is
                //to check if the thread id same or not.
                if (entry.getKey().equals(processId)) {
                    hasProcessId = true;
                    ProcessIdHelper processHelper = entry.getValue();
                    List<ThreadIdHelper> threadList = processHelper.getThreadIdHelperList();

                    boolean hasThreadId = false;
                    for (ThreadIdHelper threadHelper : threadList) {
                        if (threadHelper.getThreadId().equals(threadId)) {
                            hasThreadId = true;
                            threadHelper.getLineContentList().add(line.getContent());
                        }
                    }

                    if (!hasThreadId) {
                        processHelper.getThreadIdHelperList().add(new ThreadIdHelper(threadId, line));
                    }
                }
            }
            if (!hasProcessId) {
                addProcessIdInMap(processId, threadId, line);
            }
        }
    }

    private void addProcessIdInMap(String processId, String threadId, Line line) {
        ThreadIdHelper thread = new ThreadIdHelper(threadId, line);
        ProcessIdHelper process = new ProcessIdHelper(processId, thread);
        splitMap.put(processId, process);
    }

    @Data
    class ProcessIdHelper {
        private String processId;
        private List<ThreadIdHelper> threadIdHelperList = new ArrayList<>();

        ProcessIdHelper(String processId, ThreadIdHelper threadIdHelper) {
            this.processId = processId;
            threadIdHelperList.add(threadIdHelper);
        }
    }

    @Data
    class ThreadIdHelper {
        private String threadId;
        private List<String> lineContentList = new ArrayList<>();

        ThreadIdHelper(String threadId, Line line) {
            this.threadId = threadId;
            lineContentList.add(line.getContent());
        }
    }

    DefaultInputFormatProvider getProvider() {
        return getDefaultProvider(new String[]{"DEFAULT"});
    }
}