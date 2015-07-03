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
import org.g6.laas.core.rule.action.RuleAction;
import org.g6.laas.sm.log.DBQueryLine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class TopNQueryTask extends AbstractAnalysisTask<Map<Double, Line>> {

    private int N = 50;

    private Rule rule;

    private Collection<Line> lines = new ArrayList<>();

    @Override
    protected Map<Double, Line> process() {
        Map<Double, Line> result = new TreeMap<>();
        Map<Double, Line> cached = new TreeMap<>();
        for (Line line : lines) {
            SplitResult splitResult = new DBQueryLine(line).split();
            Field field = splitResult.get("execution_time");
            Double time = (Double) field.getValue();
            cached.put(time, line);
        }
        if(cached.size() <= N) return cached;
        int count = 0;
        for (Map.Entry<Double,Line> entry:cached.entrySet()) {
            if(count >= N) break;
            result.put(entry.getKey(),entry.getValue());
            count++;
        }
        return result;
    }

    public TopNQueryTask(int N,LogHandler handler) {
        this.N = N;
        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.setHandler(handler);
        rule = new KeywordRule("RTE D DBQUERY^");
        rule.addAction(new RuleAction(){
            @Override
            public void satisfied(Rule rule, Object content) {
               lines.add((Line)content);
            }
        });
        context.getRules().add(rule);
        setContext(context);
    }
}
