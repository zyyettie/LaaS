package org.g6.laas.core.engine.task.workflow;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class RuleChecker {
    private WorkFlowTasks tasks;
    private String taskDefXML;

    public List<Error> check() {
        List<Error> errors = new ArrayList<>();

        Start start = tasks.getStart();
        Error error;
        if (start == null) {
            error = new Error("START", "start is not defined in " + taskDefXML);
            errors.add(error);
        }
        //add Task check

        End end = tasks.getEnd();
        if (end == null) {
            error = new Error("END", "end is not defined in " + taskDefXML);
            errors.add(error);
        }

        return errors;
    }

    @Data
    @AllArgsConstructor
    public class Error {
        private String code;
        private String desc;
    }
}
