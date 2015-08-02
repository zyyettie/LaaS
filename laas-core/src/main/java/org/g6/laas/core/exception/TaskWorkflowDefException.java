package org.g6.laas.core.exception;


public class TaskWorkflowDefException extends LaaSCoreRuntimeException{
    public TaskWorkflowDefException(String message) {
        super(message);
    }

    public TaskWorkflowDefException(String message, Throwable cause) {
        super(message, cause);
    }
}
