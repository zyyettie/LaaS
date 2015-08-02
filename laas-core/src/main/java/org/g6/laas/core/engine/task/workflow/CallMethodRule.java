package org.g6.laas.core.engine.task.workflow;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CallMethodRule {
    private String methodName;
    private String paramCount;
    private List<ParamCreateRule> params;

    private int index;
    private String key;

    public void addParams(ParamCreateRule param){
        if(params == null)
            params = new ArrayList<>();
        params.add(param);
    }
}
