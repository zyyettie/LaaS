package org.g6.laas.sm.task;

import org.g6.laas.core.field.DateTimeField;
import org.g6.laas.core.field.IntegerField;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.result.SplitResult;
import org.g6.laas.core.log.result.SplitResultComparator;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Get the slowest and fastest request from SM-OMi log file
 *
 * @author Peter Zhang
 * @version 1.0
 * @since 1.0
 */
public class SMOMiPerformanceTask extends SMRTETask<Map<String,String>> {
    private List splitResultList = new ArrayList<SplitResult>();
    private SplitResult result = null;
    private List<String> returnList = new ArrayList<>();

    @Override
    protected Map<String,String> process() {
        //record the request as per the original order from log file
        SplitResult firstRequestLine = (SplitResult) splitResultList.get(0);
        SplitResult lastRequestLine = (SplitResult) splitResultList.get(splitResultList.size() - 1);
        DateTimeField firstRequestDate = (DateTimeField) firstRequestLine.get("datetime");
        DateTimeField lastRequestDate = (DateTimeField) lastRequestLine.get("datetime");
        double durationTemp = lastRequestDate.getValue().getTime() - firstRequestDate.getValue().getTime();
        double duration = durationTemp / 3600000.0d;
        DecimalFormat localDecimalFormat = new DecimalFormat("#######0.00");
        double tHour = splitResultList.size() / duration;
        double average = durationTemp / (splitResultList.size() * 1000);
        int totalRequestNumber = splitResultList.size();
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("totalRequestNumber",Integer.toString(totalRequestNumber));
        resultMap.put("firstRequestDate",firstRequestDate.getContent());
        resultMap.put("lastRequestDate",lastRequestDate.getContent());
        resultMap.put("duration",localDecimalFormat.format(duration));
        resultMap.put("throughput",localDecimalFormat.format(tHour));
        resultMap.put("average",localDecimalFormat.format(average));

        //Sort the SplitResult as per the execution_time ASC order
        Collections.sort(splitResultList, new SplitResultComparator());
        SplitResult slowestRequestLine = (SplitResult) splitResultList.get(splitResultList.size() - 1);
        SplitResult fastestRequestLine = (SplitResult) splitResultList.get(0);
        IntegerField slowestExecutionTimeField = (IntegerField) slowestRequestLine.get("execution_time");
        IntegerField fastestExecutionTimeField = (IntegerField) fastestRequestLine.get("execution_time");

        resultMap.put("slowestRequestTime",slowestExecutionTimeField.getValue().toString());
        resultMap.put("slowestRequestLine", ((LogFile) slowestRequestLine.getLine().getFile()).getOriginalName());
        resultMap.put("slowestRequestLineStr",slowestRequestLine.getLine().getContent());

        resultMap.put("fastestRequestTime",fastestExecutionTimeField.getValue().toString());
        resultMap.put("fastestRequestLine",((LogFile)fastestRequestLine.getLine().getFile()).getOriginalName());
        resultMap.put("fastestRequestLineStr",fastestRequestLine.getLine().getContent());
        return resultMap;
    }

    public SMOMiPerformanceTask() {
        Rule rule = new RegexRule("RTE D RADTRACE.+se.external.action\\s+RADReturn");

        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                result = line.split();
                result.setLine(line);
                splitResultList.add(result);
            }
        });
        addRule(rule);
    }

    @Override
    DefaultInputFormatProvider getProvider() {
        return getDefaultProvider(new String[]{"INTEGRATION"});
    }
}
