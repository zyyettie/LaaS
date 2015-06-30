package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.log.SplitResult;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.DefaultRuleAction;
import org.g6.laas.sm.log.DBQueryLine;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class TopNQueryTask extends AbstractAnalysisTask<Map<Double, Line>> {

    private int N = 50;

    private Rule rule;

    @Override
    protected Map<Double, Line> process() {
        Map<Double, Line> result = new TreeMap<>();
        AnalysisContext context = getContext();

        Collection<Line> lines = (Collection<Line>) context.get(rule);
        if (lines != null) {
            loop:
            for (Line line : lines) {
                SplitResult splitResult = new DBQueryLine(line).split();
                Field field = splitResult.get("execution_time");
                Double time = (Double) field.getValue();
                result.put(time, line);
                if (result.size() == N) break loop;
            }
        }

        return result;
    }

    public TopNQueryTask(int N,LogHandler handler) {
        this.N = N;
        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.setHandler(handler);
        rule = new KeywordRule("RTE D DBQUERY^");
        rule.addActionListener(new DefaultRuleAction(context));
        context.getRules().add(rule);
        setContext(context);
    }

    public TopNQueryTask(AnalysisContext context) {
        super(context);
    }
}
