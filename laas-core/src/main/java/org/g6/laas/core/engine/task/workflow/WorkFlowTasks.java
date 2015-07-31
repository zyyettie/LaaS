package org.g6.laas.core.engine.task.workflow;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WorkFlowTasks {
    private String name;
    private Start start;
    private List<Task> taskList = new ArrayList<>();
    private End end;

    public void addTask(Task task){
        taskList.add(task);
    }
}
