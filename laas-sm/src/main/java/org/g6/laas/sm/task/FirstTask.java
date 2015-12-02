package org.g6.laas.sm.task;

import lombok.Data;
import org.g6.laas.core.engine.task.ChainTask;
import org.g6.laas.core.engine.task.TaskChain;

import java.util.Map;

@Data
public class FirstTask extends ChainTask<String> {
    String name;

    @Override
    public void doTask(Map paramMap, Map output, TaskChain chain) {

        paramMap.put("FirstTask.name","Johnson");

        output.put("result", "output_from_first_task");
        chain.doTask(paramMap, output);
    }

    @Override
    protected String process() {
        return null;
    }
}
