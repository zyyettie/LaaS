package org.g6.laas.core.exception;

public class LaaSRuntimeException extends RuntimeException {
    public LaaSRuntimeException() {
        super();
    }

    public LaaSRuntimeException(String message) {
        super(message);
    }

    public LaaSRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LaaSRuntimeException(Throwable cause) {
        super(cause);
    }
}
