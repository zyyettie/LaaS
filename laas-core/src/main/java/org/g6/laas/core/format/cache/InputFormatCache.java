package org.g6.laas.core.format.cache;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.util.FileUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Map;

@Service
@Slf4j
public class InputFormatCache {
    /*@Cacheable(value = "inputFormatCache", key = "inputFormat")
    public Map<String, Map<String, LineAttributes>> getAllInputFormats() {
        Map<String, Map<String, LineAttributes>> inputFormatCache = new HashMap<>();
        Map<String, String> propMap = FileUtil.getPropertyValues("input_format.properties");

        for (Map.Entry<String, String> entry : propMap.entrySet()) {
            Map<String, LineAttributes> fileFormat = getFormatDataFromJsonFile(entry.getValue());
            inputFormatCache.put(entry.getKey(), fileFormat);
        }
        return inputFormatCache;
    }*/


    @Cacheable("inputFormats")
    public InputFormat getInputFormat(String key) {
        String formatFile = getFormatFile(key);
        return new DefaultInputFormat(new File(formatFile));
    }

    @Cacheable("formatFiles")
    public String getFormatFile(String key) {
        Map<String, String> propMap = FileUtil.getPropertyValues("input_format.properties");
        return propMap.get(key);
    }

    /*@CachePut(value = "inputFormatCache", key = "inputFormat")
    public void updateInputFormtCache() {
        removeAllInputFormats();
        getAllInputFormats();
    }

    @CacheEvict(value = "inputFormatCache", allEntries = true)
    public void removeAllInputFormats() {
        //do nothing, only remove all the cached data from cache
    }*/


}
