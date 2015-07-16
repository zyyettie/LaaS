package org.g6.laas.core.filter;

public interface IFilter {
    public boolean isFiltered(Object content);

    public IFilter and(IFilter filter);

    public IFilter or(IFilter filter);

    public IFilter not();
}
