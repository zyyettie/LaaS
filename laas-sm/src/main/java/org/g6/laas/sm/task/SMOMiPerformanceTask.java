package org.g6.laas.sm.task;

import org.g6.laas.core.field.DateTimeField;
import org.g6.laas.core.field.IntegerField;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.result.SplitResult;
import org.g6.laas.core.log.result.SplitResultComparator;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Get the slowest and fastest request from SM-OMi log file
 *
 * @author Peter Zhang
 * @version 1.0
 * @since 1.0
 */
public class SMOMiPerformanceTask extends SMRTETask<List<String>> {
    private List splitResultList = new ArrayList<SplitResult>();
    private SplitResult result = null;
    private List<String> returnList = new ArrayList<>();

    @Override
    protected List<String> process() {
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
        //System.out.println("SM handled total request number is : " + splitResultList.size());
        //System.out.println("First request finished date is : " + firstRequestDate.getContent() + ",\nLast request finished date is : " + lastRequestDate.getContent() + "\nTotal :" + localDecimalFormat.format(duration) + " hour");
        //System.out.println("SM throughput is about : " + localDecimalFormat.format(tHour) + "requests/hour, and each request spent : " + localDecimalFormat.format(average) + " seconds average.");
        int totalRequestNumber = splitResultList.size();
        returnList.add("SM handled total request number is : " + totalRequestNumber);
        returnList.add("First request finished date is : " + firstRequestDate.getContent() + ",\nLast request finished date is : " + lastRequestDate.getContent() + "\nTotal :" + localDecimalFormat.format(duration) + " hour");
        returnList.add("SM throughput is about : " + localDecimalFormat.format(tHour) + " requests/hour, and each request spent : " + localDecimalFormat.format(average) + " seconds average.");

        //Sort the SplitResult as per the execution_time ASC order
        Collections.sort(splitResultList, new SplitResultComparator());
        SplitResult slowestRequestLine = (SplitResult) splitResultList.get(splitResultList.size() - 1);
        SplitResult fastestRequestLine = (SplitResult) splitResultList.get(0);
        IntegerField slowestExecutionTimeField = (IntegerField) slowestRequestLine.get("execution_time");
        IntegerField fastestExecutionTimeField = (IntegerField) fastestRequestLine.get("execution_time");

        //System.out.println("the slowest request spent: " + slowestExecutionTimeField.getValue() + " ms, in file " + slowestRequestLine.getLine().getFile().getName() + ", with String : " + slowestRequestLine.getLine().getContent());
        //System.out.println("the fastest request spent: " + fastestExecutionTimeField.getValue() + " ms, in file " + fastestRequestLine.getLine().getFile().getName() + ", with String : " + fastestRequestLine.getLine().getContent());
        returnList.add("the slowest request spent: " + slowestExecutionTimeField.getValue() + " ms, in file " + slowestRequestLine.getLine().getFile().getName() + ", with String : " + slowestRequestLine.getLine().getContent());
        returnList.add("the fastest request spent: " + fastestExecutionTimeField.getValue() + " ms, in file " + fastestRequestLine.getLine().getFile().getName() + ", with String : " + fastestRequestLine.getLine().getContent());

        return returnList;
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
}
