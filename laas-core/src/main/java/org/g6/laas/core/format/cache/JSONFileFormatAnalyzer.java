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
import org.g6.laas.core.log.line.LineAttributes;
import org.g6.util.Constants;
import org.g6.util.JSONUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class JSONFileFormatAnalyzer {
    private File formatFile;

    JSONFileFormatAnalyzer(File formatFile) {
        this.formatFile = formatFile;
    }

    public List<LineAttributes> getFileFormatDataFromJsonFile() {
        List<LineAttributes> lineAttrList = new ArrayList<>();
        List<String> lineList;
        try {
            lineList = Files.readLines(formatFile, Charset.defaultCharset());
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
            LineAttributes lineAttr = new LineAttributes();
            lineAttr.setSplitRegex(lineFormat.getRegex());
            lineAttr.setName(lineFormat.getName());
            lineAttr.setKey(lineFormat.getKey());

            List<LogFieldFormat> fields = lineFormat.getFields();
            List<FieldFormat> tempFields = new ArrayList<>();

            for (LogFieldFormat field : fields) {
                if (field.getType().equals(Constants.FIELD_FORMAT_TYPE_DATETIME)) {
                    // if date format is not specified for a field in JSON file e.g.
                    // {"name":"datetime","type":"DateTime","sortable":"false","date_time_format": "MM/dd/yyyy HH:mm:ss"}
                    // the one defined on file level will be used
                    if (field.getDateFormat() == null) {
                        field.setDateFormat(dateFormat);
                    }
                }
                tempFields.add(field);
            }
            lineAttr.setFieldFormats(tempFields);
            lineAttrList.add(lineAttr);
        }

        return lineAttrList;
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
        private String name;
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
