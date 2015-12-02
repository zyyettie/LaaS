package org.g6.laas.sm.task;

import lombok.NoArgsConstructor;
import org.g6.laas.core.engine.task.TaskChain;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.result.SplitResult;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.laas.sm.vo.ProcessThread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
public class ProcessAndThreadOfUserTask extends SMRTETask<List<ProcessThread>> {
    private List<SplitResult> results = new ArrayList<>();

    @Override
    public List<ProcessThread> process() {
        List<ProcessThread> list = new ArrayList();

        for (SplitResult result : results) {
            ProcessThread pt = new ProcessThread();

            pt.setProcessId(((Integer)result.get("process_id").getValue()).intValue());
            pt.setThreadId(((Integer)result.get("thread_id").getValue()).intValue());
            list.add(pt);
        }
        Collections.sort(list);

        return list;
    }

    private void initRule(String userName) {
        Rule rule = new RegexRule("^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE I User\\s" + userName + "\\shas logged in and is using a.+");

        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                SplitResult result = line.split();
                results.add(result);
            }
        });
        addRule(rule);
    }

    @Override
    public void doTask(Map request, Map response, TaskChain chain) {
        this.setFiles((List<LogFile>) request.get("files"));
        String userName = request.get("userName").toString();
        initRule(userName);
        started();
        processRules();
        List<ProcessThread> result = process();
        finished();

        response.put("result", result);
        chain.doTask(request, response);
    }

    DefaultInputFormatProvider getProvider() {
        return getDefaultProvider(new String[]{"DEFAULT"});
    }
}
