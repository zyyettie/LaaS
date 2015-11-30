package org.g6.laas.sm.task;

import lombok.Data;
import org.g6.laas.core.engine.task.ChainTask;
import org.g6.laas.core.engine.task.TaskChain;

import java.util.Map;

@Data
public class SecondTask extends ChainTask<String> {
    String no;

    @Override
    public void doTask(Map paramMap, Map output, TaskChain chain) {
        String name = paramMap.get("FirstTask.name").toString();

        paramMap.put("SecondTask.no","123456");

        output.put("result","output_from_second_task");

        chain.doTask(paramMap, output);
    }

    @Override
    protected String process() {
        return null;
    }
}
