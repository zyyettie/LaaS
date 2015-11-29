package org.g6.laas.core.engine.task;

import java.util.HashMap;
import java.util.Map;

public class TaskChain extends AbstractAnalysisTask<Object> {

    public static final int INCREMENT = 10;

    private ChainTask[] tasks = new ChainTask[0];
    /**
     * This is used to receive all values from client page.
     */
    Map paramMap = new HashMap();

    Map<String, Object> outputMap = new HashMap();


    /**
     * The int which is used to maintain the current position
     * in the task chain.
     */
    private int pos = 0;

    public Object doTask(Map paramMap, Map output) {
        // Call the next task if there is one
        if (pos < n) {
            ChainTask task = tasks[pos++];
            task.doTask(paramMap, output, this);
        }
        return output;
    }

    /**
     * The int which gives the current number of tasks in the chain.
     */
    private int n = 0;

    public void addTask(ChainTask task) {
        // Prevent the same task being added multiple times
        for (ChainTask chainTask : tasks)
            if (task == chainTask)
                return;

        if (n == tasks.length) {
            ChainTask[] newTasks = new ChainTask[n + INCREMENT];
            System.arraycopy(tasks, 0, newTasks, 0, n);
            tasks = newTasks;
        }
        tasks[n++] = task;
    }

    void release() {
        for (int i = 0; i < n; i++) {
            tasks[i] = null;
        }
        n = 0;
        pos = 0;
    }

    @Override
    protected Object process() {
        return null;
    }

    @Override
    public Object analyze() {
        return doTask(paramMap, outputMap);
    }

    public void setParamMap(Map<String, Object> paramMap){
        this.paramMap = paramMap;
    }

    public void addInput(String key, Object obj){
        paramMap.put(key, obj);
    }

    public void addOutput(String key, Object obj){
        outputMap.put(key, obj);
    }
}
