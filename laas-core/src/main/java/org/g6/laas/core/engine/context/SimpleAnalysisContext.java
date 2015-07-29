package org.g6.laas.core.engine.context;


import lombok.Data;
import org.g6.laas.core.file.sorter.FileSorter;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.log.handler.LogHandler;
import org.g6.laas.core.rule.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Data
public class SimpleAnalysisContext implements AnalysisContext {

    private LogHandler handler;

    private Collection<Rule> rules = new ArrayList<>();

    private Map<Object, Object> holder = new HashMap<>();

    private InputFormat inputForm;

    private FileSorter sorter;


    @Override
    public InputFormat getInputFormat() {
        return inputForm;
    }

    @Override
    public LogHandler getHandler() {
        return handler;
    }

    @Override
    public Collection<Rule> getRules() {
        return rules;
    }

    @Override
    public void set(Object key, Object value) {
        holder.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return holder.get(key);
    }

}
