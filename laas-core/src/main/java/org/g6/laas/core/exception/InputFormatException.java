package org.g6.laas.core.exception;

public class InputFormatException extends LaaSCoreRuntimeException {

    public InputFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputFormatException(String message) {
        super(message);
    }
}
