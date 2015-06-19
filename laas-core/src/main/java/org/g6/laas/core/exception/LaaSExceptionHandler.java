package org.g6.laas.core.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LaaSExceptionHandler {
    public static void handleException(String msg, Exception e) {
        log.error(msg, e);
        throw new LaaSRuntimeException(msg, e);
    }
}
