package org.g6.laas.core.exception;

public class InputFormatNotFoundException extends LaaSCoreRuntimeException {

    public InputFormatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public InputFormatNotFoundException(String message) {
        super(message);
    }
}
