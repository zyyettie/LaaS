package org.g6.laas.core.engine.task.workflow;

import lombok.Data;

@Data
public class TaskInputRule {
    private String name;
    private CallMethodRule callMethodRule;
}
