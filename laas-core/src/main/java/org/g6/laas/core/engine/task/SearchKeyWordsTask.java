package org.g6.laas.core.engine.task;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.log.handler.LogHandler;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.DefaultRuleAction;
import org.g6.laas.core.rule.action.RuleAction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SearchKeyWordsTask extends AbstractAnalysisTask<Map<Rule, Collection<Line>>> {

    private Collection<Rule> rules;

    public SearchKeyWordsTask(Collection<Rule> rules, LogHandler handler) {
        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.setHandler(handler);
        this.rules = rules;
        RuleAction action = new DefaultRuleAction(context);
        for (Rule rule : this.rules) {
            rule.addAction(action);
        }
        context.setRules(this.rules);
        setContext(context);
    }

    @Override
    protected Map<Rule, Collection<Line>> process() {
        Map<Rule, Collection<Line>> result = new HashMap<>();
        AnalysisContext context = this.getContext();
        for (Rule rule : rules) {
            result.put(rule, (Collection<Line>) context.get(rule));
        }
        return result;
    }
}
