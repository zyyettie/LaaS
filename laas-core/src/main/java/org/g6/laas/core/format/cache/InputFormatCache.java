package org.g6.laas.core.format.cache;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.util.FileUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
@Slf4j
public class InputFormatCache {

    @Cacheable("inputFormats")
    public InputFormat getInputFormat(String key) {
        String formatFile = getFormatFile(key);
        return new DefaultInputFormat(FileUtil.getFile(formatFile));
    }

    @Cacheable("formatFiles")
    public String getFormatFile(String key) {
        Map<String, String> propMap = FileUtil.getPropertyValues("input_format.properties");
        return propMap.get(key);
    }


    @CacheEvict(value = "formatFiles", allEntries = true)
    public void removeAllInputFormats() {
        //do nothing, only remove all the cached data from cache
    }


}
