package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;

import java.util.ArrayList;
import java.util.Collection;

public class TopNQueryTask extends AbstractAnalysisTask<Collection<Line>> {
    private int N = 50;
    private Rule rule;

    private Collection<Line> lines = new ArrayList<>();

    @Override
    protected Collection<Line> process() {
        return null;
    }

    public TopNQueryTask(int N, LogHandler handler) {
        this.N = N;
        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.setHandler(handler);
        rule = new KeywordRule("RTE D DBQUERY^");
        //here not need to specify action for rules
        context.getRules().add(rule);
        setContext(context);
    }
}
