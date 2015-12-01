package org.g6.laas.sm.task;

import org.g6.laas.core.engine.task.TaskChain;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.laas.sm.vo.LoginInfo;
import org.g6.laas.sm.vo.ProcessThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginUserInfoTask extends SMRTETask<LoginInfo> {
    private List<Line> lines = new ArrayList();

    @Override
    protected LoginInfo process() {

        return null;
    }

    private void initRules(ProcessThread pt) {
        Rule rule = new RegexRule("^\\s*" + pt.getProcessId() + "\\(\\s+" + pt.getThreadId() + "\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE.+")
                .and(
                        new RegexRule("license")
                                .or(new RegexRule(""))
                                .or(new RegexRule(""))
                );

        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                lines.add(line);
            }
        });
        addRule(rule);
    }

    @Override
    public void doTask(Map request, Map response, TaskChain chain) {
        List<ProcessThread> ptList = (List<ProcessThread>) response.get("result");
        ProcessThread pt = ptList.get(0);
        initRules(pt);
        LoginInfo info = process();
    }
}
