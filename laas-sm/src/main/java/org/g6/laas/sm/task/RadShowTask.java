package org.g6.laas.sm.task;

import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.unit.LineSetUnit;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.laas.sm.log.unit.SMJSLineUnit;
import org.g6.laas.sm.log.unit.SMRadLineSetUnit;
import org.g6.laas.sm.log.unit.SMRadLineUnit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadShowTask extends SMRTETask<LineSetUnit> {
    private List<Line> lines = new ArrayList<>();
    private Pattern radPattern = Pattern.compile("^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D RADTRACE.+\\]\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)");
    private Pattern jsPattern = Pattern.compile("^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D SCRIPTTRACE:\\s+([^\\s]+)\\s+entered,");

    @Override
    protected LineSetUnit process() {
        //String regex = "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D RADTRACE.+\\]\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)";
        //Pattern pattern = Pattern.compile(regex);
        SMRadLineSetUnit allSet = new SMRadLineSetUnit();
        while (lines.size() > 0) {
            Iterator<Line> it = lines.iterator();
            SMRadLineSetUnit set = new SMRadLineSetUnit();
            set = constructSetUnit(set, it);
            allSet.addUnit(set);
        }
        return allSet;
    }

    private SMRadLineSetUnit constructSetUnit( SMRadLineSetUnit set, Iterator<Line> it) {
        while(it.hasNext()) {
            Line line = it.next();
            Matcher radMatcher = radPattern.matcher(line.getContent());
            boolean isRadMatched = radMatcher.find();
            Matcher jsMatcher = jsPattern.matcher(line.getContent());
            boolean isJsMatched = jsMatcher.find();
            if (isRadMatched || isJsMatched ) {
                int processId = Integer.parseInt(isRadMatched ? radMatcher.group(1) : jsMatcher.group(1));
                int threadId = Integer.parseInt(isRadMatched ? radMatcher.group(2) : jsMatcher.group(2));
                if (set.getProcessId() < 0 || set.getThreadId() < 0) {
                    set.setProcessId(processId);
                    set.setThreadId(threadId);
                    it.remove();
                } else if (set.getProcessId() == processId && set.getThreadId() == threadId) {
                    it.remove();
                } else {
                    continue;
                }

                if (isJsMatched) {
                    String jsName = jsMatcher.group(4);
                    SMJSLineUnit unit = new SMJSLineUnit(line);
                    unit.setJsName(jsName);
                    set.addUnit(unit);
                    continue;
                }

                String radName = radMatcher.group(4);
                String panelName = radMatcher.group(5);
                String panelType = radMatcher.group(6);
                SMRadLineUnit unit = new SMRadLineUnit(line);
                unit.setRadName(radName);
                unit.setPanelName(panelName);
                unit.setPanelType(panelType);
                if (panelName.equals("start")) {
                    SMRadLineSetUnit subSet = new SMRadLineSetUnit();
                    subSet.setRadName(radName);
                    subSet.setLevel(set.getLevel()+1);
                    subSet.addUnit(unit);
                    set.addUnit(subSet);
                    constructSetUnit(subSet, it);
                } else if (panelName.equals("RADReturn") || panelName.equals("RADNullExit")) {
                    set.addUnit(unit);
                    return set;
                } else {
                    set.addUnit(unit);
                }
            } else {
                it.remove();
            }
        }
        return set;
    }

    public RadShowTask() {
        Rule rule = new KeywordRule("RTE D RADTRACE").or(new KeywordRule("RTE D SCRIPTTRACE:"));
        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                line.split();
                lines.add(line);
            }
        });
        addRule(rule);
    }
}
