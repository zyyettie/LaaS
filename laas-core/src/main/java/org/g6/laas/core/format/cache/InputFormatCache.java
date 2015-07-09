package org.g6.laas.core.format.cache;

import org.g6.laas.core.format.InputFormat;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Map;

public class InputFormatCache {
    @Cacheable(value = "inputFormatCache", key = "inputFormat")
    public Map<String, InputFormat> getAllInputFormats() {

        return null;
    }

    @CachePut(value = "inputFormatCache", key = "inputFormat")
    public void updateInputFormtCache() {

    }
    @CacheEvict(value="inputFormatCache",allEntries=true)
    public void removeAllInputFormats(){

    }
}
