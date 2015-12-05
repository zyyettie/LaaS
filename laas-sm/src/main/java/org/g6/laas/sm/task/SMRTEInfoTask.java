package org.g6.laas.sm.task;

import org.g6.laas.core.engine.task.TaskChain;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.laas.sm.vo.LoginInfo;
import org.g6.laas.sm.vo.ProcessThread;
import org.g6.laas.sm.vo.SMRTEInfo;
import org.g6.util.RegexUtil;

import java.util.*;

public class SMRTEInfoTask extends SMRTETask<SMRTEInfo> {
    private Set<String> lines = new HashSet<>();

    //Only printed after login
    private final String LICENSE_TYPE = "^[\\s:/\\w\\(\\)]+RTE I User (\\S+) has logged in and is using a (\\w+) license";
    private final String WEB_CLIENT_INFO = "^[\\s:/\\w\\(\\)]+RTE I SOAP client information scguiwweb\\s+([0-9\\.]+)\\s+.+AppServer (.+)";
    private final String WIN_CLIENT_INFO = "^[\\s:/\\w\\(\\)]+RTE I SOAP client information scguiwswt\\s+([0-9\\.]+)\\s+";
    private final String TSO = "^[\\s:/\\w\\(\\)]+RTE I Set trusted sign-on login user to\\s";
    private final String CAC = "^[\\s:/\\w\\(\\)]+RTE I Set CAC login user to\\s";

    //printed after stating SM server no matter whether user logs in
    private final String SQL_SERVER_INFO = "^[\\s:/\\w\\(\\)]+RTE I (MS SQL Server).+(case\\s[a-z]+),.+";
    private final String ORACLE_INFO_1 = "^[\\s:/\\w\\(\\)]+RTE I (Oracle) instance setting for NLS_SORT is set to\\s([A-Z]+)";
    private final String ORACLE_INFO_2 = "^[\\s:/\\w\\(\\)]+RTE I (Oracle) instance setting for NLS_SORT could not be determined. Default to\\s([A-Z]+)";
    private final String APPS_VERSION = "^[\\s:/\\w\\(\\)]+RTE I Application Version:\\s([\\d.]+)";
    private final String SERVER_VERSION = "^[\\s:/\\w\\(\\)]+RTE I Process sm\\s([\\d.]+).+System:\\s([\\d]+).+";
    private final String FIPS = "^[\\s:/\\w\\(\\)]+RTE I FIPS MODE is set successfully!";

    private final String DEBUGHTTP_ENABLED = "^[\\s:/\\w\\(\\)]+RTE D Parsing request document.+";
    private final String RTM3_ENABLED = "^[\\s:/\\w\\(\\)]+RTE D RADTRACE.+";
    private final String DEBUGDBQUERY_ENABLED = "^[\\s:/\\w\\(\\)]+RTE D [DBFIND|DBQUERY].+";

    @Override
    protected SMRTEInfo process() {
        LoginInfo loginInfo = new LoginInfo();
        SMRTEInfo serverInfo = new SMRTEInfo();

        for (String content : lines) {
            handleLoginInfo(content, loginInfo, serverInfo);
            handleServerInfo(content, loginInfo, serverInfo);
        }
        serverInfo.setLoginInfo(loginInfo);
        return serverInfo;
    }

    private void handleServerInfo(String content, LoginInfo loginInfo, SMRTEInfo serverInfo) {
        if (RegexUtil.isMatched(content, SQL_SERVER_INFO)) {
            String[] values = RegexUtil.getValues(content, SQL_SERVER_INFO);
            serverInfo.setDbName(values[0]);
            serverInfo.setDbCase(values[1]);
        } else if (RegexUtil.isMatched(content, ORACLE_INFO_1)) {
            String[] values = RegexUtil.getValues(content, ORACLE_INFO_1);
            serverInfo.setDbName(values[0]);
            if(values[1].equalsIgnoreCase("binary")){
                serverInfo.setDbCase("case sensitive");
            }else{
                serverInfo.setDbCase("case insensitive");
            }
        }else if (RegexUtil.isMatched(content, ORACLE_INFO_2)) {
            String[] values = RegexUtil.getValues(content, ORACLE_INFO_2);
            serverInfo.setDbName(values[0]);
            if(values[1].equalsIgnoreCase("binary")){
                serverInfo.setDbCase("case sensitive");
            }else{
                serverInfo.setDbCase("case insensitive");
            }
        }else if (RegexUtil.isMatched(content, APPS_VERSION)) {
            String[] values = RegexUtil.getValues(content, APPS_VERSION);
            serverInfo.setAppsVersion(values[0]);
        } else if (RegexUtil.isMatched(content, SERVER_VERSION)) {
            String[] values = RegexUtil.getValues(content, SERVER_VERSION);
            serverInfo.setServerVersion(values[0]);
        } else if (RegexUtil.isMatched(content, FIPS)) {
            serverInfo.setFIPs(true);
        }
    }

    private void handleLoginInfo(String content, LoginInfo loginInfo, SMRTEInfo serverInfo) {
        if (RegexUtil.isMatched(content, LICENSE_TYPE)) {
            String[] values = RegexUtil.getValues(content, LICENSE_TYPE);
            loginInfo.setLicenseType(values[1]);
        } else if (RegexUtil.isMatched(content, WEB_CLIENT_INFO)) {
            String[] values = RegexUtil.getValues(content, WEB_CLIENT_INFO);
            loginInfo.setClientType("Web Client");
            loginInfo.setClientVersion(values[0]);
            loginInfo.setWebServer(values[1]);
            serverInfo.setWebServer(values[1]);
        } else if (RegexUtil.isMatched(content, WIN_CLIENT_INFO)) {
            String[] values = RegexUtil.getValues(content, WIN_CLIENT_INFO);
            loginInfo.setClientType("Win Client");
            loginInfo.setClientVersion(values[0]);
        } else if (RegexUtil.isMatched(content, TSO)) {
            loginInfo.setTSO(true);
            serverInfo.setTSO(true);
        } else if (RegexUtil.isMatched(content, CAC)) {
            loginInfo.setCAC(true);
            serverInfo.setTSO(true);
        }else if (RegexUtil.isMatched(content, DEBUGHTTP_ENABLED)) {
            serverInfo.setDebughttp(true);
        }else if (RegexUtil.isMatched(content, RTM3_ENABLED)) {
            serverInfo.setRtm3(true);
        }else if (RegexUtil.isMatched(content, DEBUGDBQUERY_ENABLED)) {
            serverInfo.setDebugdbquery(true);
        }
    }

    private void initRules(ProcessThread pt) {
        Rule serverRule = new RegexRule(SQL_SERVER_INFO)
                .or(new RegexRule(ORACLE_INFO_1))
                .or(new RegexRule(ORACLE_INFO_2))
                .or(new RegexRule(APPS_VERSION))
                .or(new RegexRule(SERVER_VERSION))
                .or(new RegexRule(FIPS))
                .or(new RegexRule(DEBUGHTTP_ENABLED))
                .or(new RegexRule(RTM3_ENABLED))
                .or(new RegexRule(DEBUGDBQUERY_ENABLED));
        Rule finalRule = serverRule;
        if (pt != null) {
            Rule loginRule = new RegexRule("^\\s*" + pt.getProcessId() + "\\(\\s+" + pt.getThreadId() + "\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE.+")
                    .and(
                            new RegexRule(LICENSE_TYPE)
                                    .or(new RegexRule(WEB_CLIENT_INFO))
                                    .or(new RegexRule(WIN_CLIENT_INFO))
                                    .or(new RegexRule(TSO))
                                    .or(new RegexRule(CAC))
                    );
            finalRule = serverRule.or(loginRule);
        }
        finalRule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                lines.add(line.getContent());
            }
        });
        addRule(finalRule);
    }

    @Override
    public void doTask(Map request, Map response, TaskChain chain) {
        this.setFiles((List<LogFile>) request.get("files"));
        List<ProcessThread> ptList = (List<ProcessThread>) response.get("result");
        ProcessThread pt = null;
        if (ptList != null && !ptList.isEmpty()) {
            pt = ptList.get(0);
        }

        initRules(pt);
        started();
        processRules();
        SMRTEInfo result = process();
        finished();

        response.put("result", result);

        chain.doTask(request, response);
    }
}
