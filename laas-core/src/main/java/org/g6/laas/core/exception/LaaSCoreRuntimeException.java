package org.g6.laas.core.exception;

public class LaaSCoreRuntimeException extends RuntimeException {
    public LaaSCoreRuntimeException() {
        super();
    }

    public LaaSCoreRuntimeException(String message) {
        super(message);
    }

    public LaaSCoreRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public LaaSCoreRuntimeException(Throwable cause) {
        super(cause);
    }
}
