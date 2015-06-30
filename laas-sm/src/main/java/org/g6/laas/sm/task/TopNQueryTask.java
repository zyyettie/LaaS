package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.DefaultRuleAction;
import org.g6.laas.sm.log.DBQueryLine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TopNQueryTask extends AbstractAnalysisTask<Map<Line, Double>> {

    private int N = 50;

    @Override
    protected Map<Line, Double> process() {
        Map<Line, Double> result = new HashMap<>();
        AnalysisContext context = getContext();
        for (Rule rule : context.getRules()) {
            Collection<Line> lines = (Collection<Line>) context.get(rule);
            for (Line line : lines) {
                Collection<Field> fields = new DBQueryLine(line).split();
                for (Field field : fields) {
                    if ("execution_time".equals(field.getName())) {
                        Double time = (Double) field.getValue();
                        result.put(line, time);
                    }
                }
            }
        }
        //todo sort by time that return
        return result;
    }

    public TopNQueryTask(int N) {
        this.N = N;
        AnalysisContext context = new SimpleAnalysisContext();
        Rule rule = new KeywordRule("RTE D DBQUERY^");
        rule.addActionListener(new DefaultRuleAction(context));
        context.getRules().add(rule);
        this.setContext(context);
    }
}
