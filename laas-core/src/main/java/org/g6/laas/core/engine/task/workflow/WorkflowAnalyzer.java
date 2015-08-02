package org.g6.laas.core.engine.task.workflow;

import lombok.Data;
import org.g6.laas.core.engine.AnalysisEngine;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.engine.task.workflow.RuleChecker.Error;
import org.g6.laas.core.exception.LaaSExceptionHandler;
import org.g6.laas.core.exception.TaskWorkflowDefException;
import org.g6.util.XMLUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Data
public class WorkflowAnalyzer {
    AnalysisEngine taskEngine;

    public Object execute(Object obj) {
        String workflowRuleFile = "org/g6/laas/core/engine/task/workflow/workflow-task-rule.xml";
        String workflowDefFile = "workflow-tasks.xml";
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

        Object outputObj = obj;
        for (Task task : linkedTasks) {
            try {
                Class taskClass = Class.forName(task.getClassName());

                CallMethodRule callMethod = task.getTaskInputRule().getCallMethodRule(); // this is for input method
                //check if the input parameters are required or not
                if (task.getTaskNoInput() != null || (task.getTaskNoInput() == null && task.getTaskInputRule() == null)) {
                    //no input for the current task, do nothing
                } else {
                    //need to handle the input. the input can accept two kinds of outputs
                    //1. the input of the first task should be from external
                    //2. the input of the other tasks should be from the output of the previous one
                    Method m = taskClass.getDeclaredMethod(callMethod.getMethodName(), outputObj.getClass());
                    m.invoke(taskClass, outputObj);
                }


                Object taskObj = taskClass.newInstance();
                Future future = taskEngine.submit((AbstractAnalysisTask)taskObj);
                Object retObj = future.get();

                if(task.getNextTask().getName().equals(end)){
                    return retObj;
                }else{
                    outputObj = retObj;
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
        //new WorkflowAnalyzer().execute();
    }
}
