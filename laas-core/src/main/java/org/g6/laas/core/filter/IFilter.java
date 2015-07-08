package org.g6.laas.core.filter;

import org.g6.laas.core.rule.Rule;

public interface IFilter {
    public boolean isFiltered(Object content);

    public Rule getRule();

    public void setRule(Rule rule);

    public IFilter and(IFilter filter);

    public IFilter or(IFilter filter);

    public IFilter not(IFilter filter);
}
