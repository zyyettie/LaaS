package org.g6.bigdata.exception;

import org.g6.laas.core.exception.LaaSRuntimeException;

public class BigDataRuntimeException extends LaaSRuntimeException{

    public BigDataRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BigDataRuntimeException(Throwable cause) {
        super(cause);
    }
}
