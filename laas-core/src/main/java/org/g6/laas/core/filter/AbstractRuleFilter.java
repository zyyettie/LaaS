package org.g6.laas.core.filter;

import org.g6.laas.core.rule.Rule;

public abstract class AbstractRuleFilter implements IFilter {
    Rule rule;

    @Override
    public boolean isFiltered(Object content) {
        if (rule != null)
            return rule.isSatisfied(content);

        return false;
    }

    @Override
    public Rule getRule() {
        return this.rule;
    }

    @Override
    public void setRule(Rule rule) {
        this.rule = rule;
    }

    @Override
    public IFilter and(IFilter filter) {
        this.rule = this.rule.and(filter.getRule());
        return this;
    }

    @Override
    public IFilter or(IFilter filter) {
        this.rule = this.rule.or(filter.getRule());
        return this;
    }

    @Override
    public IFilter not(IFilter filter) {
        this.rule = this.rule.not();
        return this;
    }
}
