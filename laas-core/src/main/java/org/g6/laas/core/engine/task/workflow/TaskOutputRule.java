package org.g6.laas.core.engine.task.workflow;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskOutputRule {
    private String name;
    private String className;
    List<CallMethodRule> callMethodRules = new ArrayList<>();


    public void addCallMethodRule(CallMethodRule callMethodRule) {
        callMethodRules.add(callMethodRule);
    }
}
