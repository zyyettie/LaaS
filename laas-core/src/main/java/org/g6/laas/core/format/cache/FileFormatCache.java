package org.g6.laas.core.format.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.log.line.LineAttributes;
import org.g6.util.FileUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Data
public class FileFormatCache {

    @Cacheable("inputFormats")
    public List<LineAttributes> getFileFormat(String key) {
        String formatFile = getFormatFile(key);
        JSONFileFormatAnalyzer analyzer = new JSONFileFormatAnalyzer(FileUtil.getFile(formatFile));

        return analyzer.getFileFormatDataFromJsonFile();
    }

    @Cacheable("formatFiles")
    public String getFormatFile(String key) {
        Map<String, String> propMap = FileUtil.getPropertyValues("/input_format.properties");
        return propMap.get(key);
    }


    @CacheEvict(value = "formatFiles", allEntries = true)
    public void removeAllInputFormats() {
        //do nothing, only remove all the cached data from cache
    }


}
