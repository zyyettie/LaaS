package org.g6.laas.core.format.cache;

import com.google.common.io.Files;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.format.FieldFormat;
import org.g6.laas.core.log.LineAttributes;
import org.g6.util.Constants;
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InputFormatCache {
    @Cacheable(value = "inputFormatCache", key = "inputFormat")
    public Map<String, Map<String, LineAttributes>> getAllInputFormats() {
        Map<String, Map<String, LineAttributes>> inputFormatCache = new HashMap<>();
        Map<String, String> propMap = FileUtil.getPropertyValues("input_format.properties");

        for (Map.Entry<String, String> entry : propMap.entrySet()) {
            Map<String, LineAttributes> fileFormat = getFormatDataFromJsonFile(entry.getValue());
            inputFormatCache.put(entry.getKey(), fileFormat);
        }
        return inputFormatCache;
    }

    private Map<String, LineAttributes> getFormatDataFromJsonFile(String formatFile){
        Map<String, LineAttributes> lineAttrMap = new HashMap<>();
        List<String> lineList;

        try {
            lineList = Files.readLines(FileUtil.getFile(formatFile), Charset.defaultCharset());
        } catch (IOException e) {
            String errMsg = "format definition file read fail";
            log.error(errMsg);
            throw new LaaSCoreRuntimeException(errMsg, e);
        }

        String jsonStr = "";
        for (String str : lineList) {
            jsonStr += str.trim();
        }

        log.debug(jsonStr);

        JSONFileFormat<JSONLineFormat> jsonFileFormat = JSONUtil.fromJson(
                jsonStr, new TypeToken<JSONFileFormat<JSONLineFormat>>() {
        }.getType());

        String dateFormat = jsonFileFormat.getDateTimeFormat();
        List<JSONLineFormat> jsonLineFormats = jsonFileFormat.getLines();

        for (JSONLineFormat lineFormat : jsonLineFormats) {
            String key = lineFormat.getKey();
            String regex = lineFormat.getRegex();
            LineAttributes lineAttr = new LineAttributes();
            lineAttr.setSplitRegex(regex);

            List<LogFieldFormat> fields = lineFormat.getFields();
            List<FieldFormat> tempFields = new ArrayList<>();

            for (LogFieldFormat field : fields) {
                if (field.getType().equals(Constants.FIELD_FORMAT_TYPE_DATETIME)) {
                    // if date format is specified for a field in JSON file e.g.
                    // {"name":"datetime","type":"DateTime","sortable":"false","date_time_format": "MM/dd/yyyy HH:mm:ss"}
                    // the one defined on file level will be used
                    if (field.getDateFormat() == null) {
                        field.setDateFormat(dateFormat);
                    }
                }
                tempFields.add(field);
            }
            lineAttr.setFieldFormats(tempFields);
            lineAttrMap.put(key, lineAttr);
        }

        return lineAttrMap;
    }

    @CachePut(value = "inputFormatCache", key = "inputFormat")
    public void updateInputFormtCache() {
        removeAllInputFormats();
        getAllInputFormats();
    }

    @CacheEvict(value = "inputFormatCache", allEntries = true)
    public void removeAllInputFormats() {
        //do nothing, only remove all the cached data from cache
    }

    @Data
    private static class JSONFileFormat<T> implements Serializable {
        @SerializedName("file_name")
        String fileName;
        @SerializedName("date_time_format")
        String dateTimeFormat;

        private List<T> lines;

    }

    @Data
    private static class JSONLineFormat {
        private String key;
        @SerializedName("line_split_regex")
        private String regex;
        private List<LogFieldFormat> fields;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class LogFieldFormat implements FieldFormat {
        private String name;
        private String type;
        @SerializedName("date_time_format")
        private String dateFormat;
        private boolean sortable;

        public LogFieldFormat(String name, String type) {
            this(name, type, null);
        }

        public LogFieldFormat(String name, String type, String dateFormat) {
            this(name, type, dateFormat, false);
        }

        public LogFieldFormat(String name, String type, boolean sortable) {
            this(name, type, null, sortable);
        }
    }
}
