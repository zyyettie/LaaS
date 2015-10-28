package org.g6.laas.core.exception;

public class LaaSValidationException extends LaaSCoreRuntimeException {
    public LaaSValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public LaaSValidationException(String message) {
        super(message);
    }
}
