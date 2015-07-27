package org.g6.laas.sm.task;

import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.unit.LineSetUnit;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.laas.sm.log.unit.SMRadLineSetUnit;
import org.g6.laas.sm.log.unit.SMRadLineUnit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadShowTask extends SMRTETask<LineSetUnit> {
    private List<Line> lines = new ArrayList<>();

    @Override
    protected LineSetUnit process() {
        String regex = "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D RADTRACE.+\\]\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)";
        Pattern pattern = Pattern.compile(regex);
        Iterator<Line> it = lines.iterator();
        LineSetUnit set = new SMRadLineSetUnit();
        set = constructSetUnit(set, it, pattern);
        return set;
    }

    private LineSetUnit constructSetUnit( LineSetUnit set, Iterator<Line> it, Pattern pattern) {
        while(it.hasNext()) {
            Line line = it.next();
            Matcher matcher = pattern.matcher(line.getContent());
            if (matcher.find()) {
                String radName = matcher.group(4);
                String panelName = matcher.group(5);
                String panelType = matcher.group(6);
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
                    constructSetUnit(subSet, it, pattern);
                } else if (panelName.equals("RADReturn") || panelName.equals("RADNullExit")) {
                    set.addUnit(unit);
                    return set;
                } else {
                    set.addUnit(unit);
                }
            }
        }
        return set;
    }

    public RadShowTask(String file) {
        Rule rule = new KeywordRule("RTE D RADTRACE");
        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                line.split();
                lines.add(line);
            }
        });

        initContext(file, rule);
    }
}
