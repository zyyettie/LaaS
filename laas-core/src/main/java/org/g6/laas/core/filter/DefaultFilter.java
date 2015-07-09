package org.g6.laas.core.filter;

public class DefaultFilter extends AbstractRuleFilter {
    public boolean isFiltered(Object content) {
        return false;
    }

    public IFilter and(IFilter filter) {
        return this;
    }

    public IFilter or(IFilter filter) {
        return this;
    }

    public IFilter not() {
        return this;
    }
}
