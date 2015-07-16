package org.g6.laas.core.format;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.cache.InputFormatCache;

@Slf4j
public final class DefaultFormatFactory {

    private static InputFormatCache cache = new InputFormatCache();

    public static InputFormat getInputFormat(String key) {
        return cache.getInputFormat(key);
    }
}
