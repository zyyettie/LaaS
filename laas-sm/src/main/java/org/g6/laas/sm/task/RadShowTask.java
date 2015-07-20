package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.format.provider.FormatProvider;
import org.g6.laas.core.log.handler.ConcreteLogHandler;
import org.g6.laas.core.log.handler.LogHandler;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.line.Slice;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.laas.sm.log.line.SMRadSlice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadShowTask extends SMRTETask<Slice> {
    private List<Line> lines = new ArrayList<>();

    @Override
    protected Slice process() {
        String regex = "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D RADTRACE.+\\]\\s+([^\\s]+)\\s+([^\\s]+)\\s+([^\\s]+)";
        Pattern pattern = Pattern.compile(regex);
        Iterator<Line> it = lines.iterator();
        Slice slice = new SMRadSlice();
        slice = constructSlice(slice, it, pattern);
        return slice;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private Slice constructSlice( Slice slice, Iterator<Line> it, Pattern pattern) {
        while(it.hasNext()) {
            Line line = it.next();
            Matcher matcher = pattern.matcher(line.getContent());
            if (matcher.find()) {
                String radName = matcher.group(4);
                String panelName = matcher.group(5);
                String panelType = matcher.group(6);
                if (panelName.equals("start")) {
                    SMRadSlice subSlice = new SMRadSlice();
                    subSlice.setRadName(radName);
                    subSlice.addLine(line);
                    slice.addLine(subSlice);
                    constructSlice(subSlice, it, pattern);
                } else if (panelName.equals("RADReturn") || panelName.equals("RADNullExit")) {
                    slice.addLine(line);
                    return slice;
                } else {
                    slice.addLine(line);
                }
            }
        }
        return slice;
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
