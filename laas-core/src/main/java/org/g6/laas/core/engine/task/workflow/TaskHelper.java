package org.g6.laas.core.engine.task.workflow;


import org.g6.laas.core.exception.TaskWorkflowDefException;
import org.g6.util.XMLUtil;

import java.util.List;

public class TaskHelper {
    private final String workFlowRuleFile = "org/g6/laas/core/engine/task/workflow/workflow-task-rule.xml";
    private String workFlowDefFile;
    private Start start;
    private End end;
    private TaskChain taskChain;

    public TaskHelper(String workFlowDefFile) {
        this.workFlowDefFile = workFlowDefFile;
        analyze();
    }

    private void analyze() {
        WorkFlowTasks workflow = XMLUtil.parse(workFlowRuleFile, workFlowDefFile);

        RuleChecker checker = new RuleChecker(workflow, workFlowDefFile);
        List<RuleChecker.Error> errors = checker.check();
        if (!errors.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            sb.append("The definition of workflow xml is not correct, errors: ");
            for (RuleChecker.Error error : errors) {
                sb.append(error.getCode()).append(" ").append(error.getDesc()).append("\n");
            }

            throw new TaskWorkflowDefException(sb.toString());
        }
        start = workflow.getStart();
        end = workflow.getEnd();
        taskChain = new TaskChain();

        List<Task> tasks = workflow.getTasks();
        List<Task> linkedTasks = this.taskChain.getLinkedList();

        String taskName = this.start.getTo();
        int size = tasks.size();

        for (int i = 0; i < size; i++) {
            Task task = getTaskByName(taskName, tasks);
            linkedTasks.add(task);
            taskName = task.getNextTask().getName();
            if (taskName.equals(this.end.getName()))
                break;
        }
    }

    private Task getTaskByName(String name, List<Task> tasks) {
        for (Task task : tasks) {
            if (task.getName().equals(name)) {
                return task;
            }
        }
        return null;
    }

    public End getEnd() {
        return end;
    }

    public Start getStart() {
        return start;
    }

    public TaskChain getTaskChain() {
        return taskChain;
    }
}
