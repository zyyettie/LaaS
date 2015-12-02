package org.g6.laas.sm.task;

import org.g6.laas.core.engine.task.TaskChain;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.laas.sm.vo.LoginInfo;
import org.g6.laas.sm.vo.ProcessThread;
import org.g6.util.RegexUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginUserInfoTask extends SMRTETask<LoginInfo> {
    private List<Line> lines = new ArrayList();
    private final String licenseType = "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE I User (\\S+) has logged in and is using a (\\w+) license";
    private final String webClientInfo = "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE I SOAP client information (\\w+)\\s+ ([0-9\\.]+)\\s+.+AppServer (.+)";
    private final String winClientInfo = "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE I SOAP client information (\\w+)\\s+([0-9\\.]+)\\s+";

    @Override
    protected LoginInfo process() {
        LoginInfo info = new LoginInfo();
        for(Line line : lines){
           String content = line.getContent();
            if(RegexUtil.isMatched(content, licenseType)){
                info.setLicenseType((RegexUtil.getValues(content, licenseType))[4]);
            }else if(RegexUtil.isMatched(content, webClientInfo)){
                String[] values = RegexUtil.getValues(content, webClientInfo);
                info.setClientType("Web Client");
                info.setClientVersion(values[4]);
                info.setWebServer(values[5]);
            }else if(RegexUtil.isMatched(content, winClientInfo)){
                String[] values = RegexUtil.getValues(content, winClientInfo);
                info.setClientType("Win Client");
                info.setClientVersion(values[4]);
            }
        }
        return info;
    }

    private void initRules(ProcessThread pt) {
        Rule rule = new RegexRule("^\\s*" + pt.getProcessId() + "\\(\\s+" + pt.getThreadId() + "\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE.+")
                .and(
                        new RegexRule(licenseType)
                                .or(new RegexRule(webClientInfo))
                                .or(new RegexRule(winClientInfo))
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
        this.setFiles((List<LogFile>) request.get("files"));
        List<ProcessThread> ptList = (List<ProcessThread>) response.get("result");
        ProcessThread pt = ptList.get(0);
        initRules(pt);
        started();
        processRules();
        LoginInfo result = process();
        finished();

        response.put("result", result);
        chain.doTask(request, response);
    }
}
