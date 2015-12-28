package org.g6.caas.exception;

public class CaaSRuntimeException extends RuntimeException {

    public CaaSRuntimeException(String message) {
        super(message);
    }

    public CaaSRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

}
