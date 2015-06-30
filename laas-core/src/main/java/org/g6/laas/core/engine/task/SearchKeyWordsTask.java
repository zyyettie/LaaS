package org.g6.laas.core.engine.task;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.DefaultRuleAction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SearchKeyWordsTask extends AbstractAnalysisTask<Map<Rule, Collection<Line>>> {

    @Override
    protected Map<Rule, Collection<Line>> process() {
        Map<Rule, Collection<Line>> result = new HashMap<>();
        AnalysisContext context = this.getContext();
        for (Rule rule : context.getRules()) {
            result.put(rule, (Collection<Line>) context.get(rule));
        }
        return result;
    }

    public SearchKeyWordsTask(Collection<Rule> rules, LogHandler handler) {
        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.setHandler(handler);
        for (Rule rule : rules) {
            rule.addActionListener(new DefaultRuleAction(context));
            context.getRules().add(rule);
        }
        this.setContext(context);
    }

    public SearchKeyWordsTask(AnalysisContext context) {
        super(context);
    }
}
