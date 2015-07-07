package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.format.json.FormatProvider;
import org.g6.laas.core.format.json.JSONFormatProvider;
import org.g6.laas.core.log.BasicLogHandler;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LineComparator;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopNQueryTask extends AbstractAnalysisTask<List<Line>> {
    private int N = 50;
    private List<Line> lines = new ArrayList<>();

    @Override
    protected List<Line> process() {
        Collections.sort(lines, new LineComparator());

        List<Line> topNList = new ArrayList<>();

        int counter = 0;
        for (Line line : lines) {
            if (counter < N) {
                topNList.add(line);
            }
            counter++;
        }
        return topNList;
    }

    public TopNQueryTask(int topN, String file) {
        this.N = topN;
        ILogFile logFile = new LogFile(file);
        Rule rule = new KeywordRule("RTE D DBQUERY");

        LogHandler handler = new BasicLogHandler(logFile, rule);
        FormatProvider provider = new JSONFormatProvider("/sm_rte_log.json");

        SimpleAnalysisContext context = new SimpleAnalysisContext();

        context.setHandler(handler);
        context.setInputForm(provider.getInputFormat());

        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line)content;
                line.split();
                lines.add(line);
            }
        });
        context.getRules().add(rule);

        setContext(context);
    }
}
