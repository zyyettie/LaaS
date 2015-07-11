package org.g6.laas.core.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LaaSExceptionHandler {

    public static void handleException(String msg) {
        log.error(msg);
        throw new LaaSRuntimeException(msg);
    }

    public static void handleException(String msg, Exception e) {
        handleException(msg, e, true);
    }

    public static void handleException(String msg, Exception e, boolean isThrown) {
        log.error(msg, e);
        if (isThrown)
            throw new LaaSRuntimeException(msg, e);
    }
}
