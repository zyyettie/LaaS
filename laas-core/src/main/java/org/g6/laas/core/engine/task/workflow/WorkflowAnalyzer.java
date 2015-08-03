package org.g6.laas.core.engine.task.workflow;

import lombok.Data;
import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.workflow.RuleChecker.Error;
import org.g6.laas.core.exception.LaaSExceptionHandler;
import org.g6.laas.core.exception.TaskWorkflowDefException;
import org.g6.util.XMLUtil;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

@Data
public class WorkflowAnalyzer {
    StrategyAnalysisEngine taskEngine;

    void init(){
        taskEngine = new StrategyAnalysisEngine();
        taskEngine.setStrategy(new ThreadPoolExecutionStrategy());
    }


    public Map<String, Object> execute(Map<String, Object> objMap) {
        init();
        String workflowRuleFile = "org/g6/laas/core/engine/task/workflow/workflow-task-rule.xml";
        String workflowDefFile = "student-workflow.xml";
        WorkFlowTasks workflow = XMLUtil.parse(workflowRuleFile, workflowDefFile);

        RuleChecker checker = new RuleChecker(workflow, workflowDefFile);
        List<Error> errors = checker.check();
        if (!errors.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append("The definition of workflow xml is not correct, errors: ");
            for (Error error : errors) {
                sb.append(error.getCode()).append(" ").append(error.getDesc()).append("\n");
            }

            throw new TaskWorkflowDefException(sb.toString());
        }

        String start = workflow.getStart().getTo();
        String end = workflow.getEnd().getName();
        List<Task> tasks = workflow.getTasks();
        TaskChain chain = new TaskChain();
        List<Task> linkedTasks = new ArrayList<>();
        String taskName = start;
        int size = tasks.size();

        for (int i = 0; i < size; i++) {
            Task task = getTaskByName(taskName, tasks);
            linkedTasks.add(task);
            taskName = task.getNextTask().getName();
            if (taskName.equals(end))
                break;
        }

        SimpleAnalysisContext context = new SimpleAnalysisContext();
        Map<String, Object> outputObj = objMap;
        for (Task task : linkedTasks) {
            try {

                Class taskClass = Class.forName(task.getClassName());

                Constructor constructor;
                Object taskObj;
                //check if the input parameters are required or not
                if (task.getTaskNoInput() != null || (task.getTaskNoInput() == null && task.getTaskInputRule() == null)) {
                    //no input for the current task, do nothing
                    constructor = taskClass.getConstructor(AnalysisContext.class);
                    taskObj = constructor.newInstance(context, outputObj);
                } else {
                    //need to handle the input. the input can accept two kinds of outputs
                    //1. the input of the first task should be from external
                    //2. the input of the other tasks should be from the output of the previous one
                    constructor = taskClass.getConstructor(AnalysisContext.class, Map.class);
                    taskObj = constructor.newInstance(context);
                }

                //TODO
                //Future future = taskEngine.submit(taskObj);
                Future future = null;
                Object retObj = future.get();

                if(task.getNextTask().getName().equals(end)){
                    return (Map<String,Object>)retObj;
                }else{
                    outputObj = (Map<String,Object>)retObj;
                }
            } catch (Exception e) {
                LaaSExceptionHandler.handleException("errors happen while executing task: " + task.getName(), e);
            }


        }
        return null;
    }

    private Task getTaskByName(String name, List<Task> tasks) {
        for (Task task : tasks) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        //TODO remove this method after testing
        //new WorkflowAnalyzer().execute();
    }
}
