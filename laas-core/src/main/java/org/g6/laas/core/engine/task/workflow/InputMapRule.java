package org.g6.laas.core.engine.task.workflow;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class InputMapRule {
    private int paramCount;
    List<ParamCreateRule> paramCreateRules;

    public void addParamCreateRule(ParamCreateRule param){
        if(paramCreateRules == null){
            paramCreateRules = new ArrayList<>();
        }
        paramCreateRules.add(param);

    }
}
