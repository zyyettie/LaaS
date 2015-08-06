package org.g6.laas.core.engine.task.workflow;

import lombok.Data;
import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AnalysisTask;
import org.g6.laas.core.engine.task.workflow.RuleChecker.Error;
import org.g6.laas.core.exception.LaaSExceptionHandler;
import org.g6.laas.core.exception.TaskWorkflowDefException;
import org.g6.util.XMLUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * new WorkflowAnalyzer("student-workflow.xml").execute(studentMap);
 */
@Data
public class WorkflowAnalyzer {
    private StrategyAnalysisEngine taskEngine;
    private SimpleAnalysisContext context = new SimpleAnalysisContext();
    private String workflowDefFile;

    public WorkflowAnalyzer(String workflowDefFile){
         this.workflowDefFile = workflowDefFile;
    }

    void init() {
        if (taskEngine == null) {
            taskEngine = new StrategyAnalysisEngine();
            taskEngine.setStrategy(new ThreadPoolExecutionStrategy());
        }
    }

    public Map<String, Object> execute(Map objMap) {
        init();

        TaskHelper helper = new TaskHelper(workflowDefFile);
        List<Task> linkedTasks = helper.getTaskChain().getLinkedList();
        Map<String, Object> outputObj = objMap;
        for (int i = 0; i < linkedTasks.size(); i++) {
            Task task = linkedTasks.get(i);
            try {
                Class taskClass = Class.forName(task.getClassName());

                Constructor constructor;
                Object taskObj;
                //check if the input parameters are required or not
                if (task.getTaskNoInput() != null) {
                    constructor = taskClass.getConstructor(AnalysisContext.class);
                    taskObj = constructor.newInstance(context);
                } else {
                    constructor = taskClass.getConstructor(AnalysisContext.class, Map.class);
                    taskObj = constructor.newInstance(context, outputObj);
                }

                Future future = taskEngine.submit((AnalysisTask) taskObj);
                Object retObj = future.get();

                doOutputCheck();

                if (task.getNextTask().getName().equals(helper.getEnd().getName())) {
                    return (Map<String, Object>) retObj;
                } else {
                    outputObj = (Map<String, Object>) retObj;
                }
            } catch (Exception e) {
                LaaSExceptionHandler.handleException("errors happen while executing task: " + task.getName(), e);
            }


        }
        throw new TaskWorkflowDefException("There should be some workflow defined in " + workflowDefFile);
    }

    private boolean doOutputCheck(){

        return true;
    }

}
