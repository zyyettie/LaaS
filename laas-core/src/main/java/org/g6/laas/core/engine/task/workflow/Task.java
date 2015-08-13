package org.g6.laas.core.engine.task.workflow;

import lombok.Data;

@Data
public class Task {
    private String name;
    private String className;
    private String description;

    private TaskNoInput taskNoInput;
    private TaskInputRule taskInputRule;
    private NextTask nextTask;
}
