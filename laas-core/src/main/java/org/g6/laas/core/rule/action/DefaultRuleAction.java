package org.g6.laas.core.rule.action;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.rule.Rule;

import java.util.ArrayList;
import java.util.List;

public class DefaultRuleAction extends AbstractRuleAction {

    public DefaultRuleAction(AnalysisContext context) {
        super(context);
    }

    @Override
    public void satisfied(Rule rule, Object content) {

        List<Line> matchedLines = (List<Line>) getContext().get(rule);
        if (matchedLines == null) {
            matchedLines = new ArrayList<>();
            matchedLines.add((Line) content);
            getContext().set(rule, matchedLines);
        } else {
            matchedLines.add((Line) content);
        }
    }
}
