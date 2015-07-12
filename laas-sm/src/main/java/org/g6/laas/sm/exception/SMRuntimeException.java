package org.g6.laas.sm.exception;

import org.g6.laas.core.exception.LaaSRuntimeException;

public class SMRuntimeException extends LaaSRuntimeException {
    public SMRuntimeException(String message) {
        super(message);
    }

    public SMRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SMRuntimeException(Throwable cause) {
        super(cause);
    }
}
