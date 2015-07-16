package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.format.DefaultFormatFactory;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.log.handler.ConcreteLogHandler;
import org.g6.laas.core.log.handler.LogHandler;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.line.LineComparator;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Get top N query from SM RTE log file
 *
 * @author Johnson Jiang
 * @version 1.0
 * @since 1.0
 */
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
        InputFormat inputFormat = DefaultFormatFactory.getInputFormat("SMRTE_SM_LOG");
        LogHandler handler = new ConcreteLogHandler(logFile);
        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                line.split();
                lines.add(line);
            }
        });

        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.setInputForm(inputFormat);
        context.setHandler(handler);
        context.getRules().add(rule);

        setContext(context);
    }
}
