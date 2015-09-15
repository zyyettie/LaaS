package org.g6.laas.core.engine.task.report;


import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ReportModel {
    private Map<String, Object> attributes = new HashMap<>();

    ReportModel(Map<String, Object> attributes) {
        attributes.putAll(attributes);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public Object getAttribute(String name, Object value) {
        return attributes.get(name);
    }

    public Map<String, Object> asMap() {
        return attributes;
    }
}
