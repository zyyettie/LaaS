package org.g6.laas.core.filter;

import org.g6.laas.core.rule.Rule;

public abstract class AbstractRuleFilter extends AbstractFilter {
    private Rule rule;

    @Override
    public boolean isFiltered(Object content) {
        if (rule != null)
            return rule.isSatisfied(content);

        return false;
    }

    public Rule getRule() {
        return this.rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
