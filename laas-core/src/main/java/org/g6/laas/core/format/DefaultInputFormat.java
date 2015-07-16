package org.g6.laas.core.format;

import com.google.common.io.Files;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.g6.laas.core.exception.InputFormatNotFoundException;
import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.exception.Regex4LineSplitNotFoundException;
import org.g6.laas.core.field.*;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.line.LineAttributes;
import org.g6.laas.core.log.line.LogLine;
import org.g6.laas.core.log.result.BasicSplitResult;
import org.g6.laas.core.log.result.SplitResult;
import org.g6.util.Constants;
import org.g6.util.JSONUtil;
import org.g6.util.RegexUtil;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

@Slf4j
@Data
@NoArgsConstructor
public final class DefaultInputFormat implements InputFormat {

    private Map<String, LineAttributes> lineAttrMap;

    public DefaultInputFormat(File jsonfile) {
        lineAttrMap = getFormatDataFromJsonFile(jsonfile);
    }

    @Override
    public SplitResult getSplits(Line line) {
        String lineSplitRegex = null;
        List<FieldFormat> fieldFormatList = null;
        List<String> errorKeyList = new ArrayList<>();
        int counter = 0;

        for (Map.Entry<String, LineAttributes> entry : lineAttrMap.entrySet()) {
            String lineFormatKey = entry.getKey();
            LineAttributes lineAttr = entry.getValue();

            if (lineFormatKey.startsWith(Constants.REGEX_PREFIX)) {// the key is regex
                String regex = lineFormatKey.substring(Constants.REGEX_PREFIX.length());
                String matchedValue = RegexUtil.getValue(line.getContent(), regex);

                if (!StringUtils.isBlank(matchedValue)) {
                    errorKeyList.add(lineFormatKey);
                    counter++;
                    fieldFormatList = lineAttr.getFieldFormats();
                    lineSplitRegex = lineAttr.getSplitRegex();
                }
            } else {
                if (line.getContent().contains(lineFormatKey)) {
                    errorKeyList.add(entry.getKey());
                    counter++;
                    fieldFormatList = lineAttr.getFieldFormats();
                    lineSplitRegex = lineAttr.getSplitRegex();
                }
            }
        }

        if (counter != 1) {
            StringBuffer sb = new StringBuffer();
            sb.append("Found more than one formats for the line which number is ")
                    .append(line.getLineNumber())
                    .append(" in ")
                    .append(line.getFile().getName())
                    .append(". The key words matched are: ");
            int keyCount = 1;
            for (String key : errorKeyList) {
                sb.append(key);
                if (errorKeyList.size() != keyCount) {
                    sb.append(", ");
                }
                keyCount++;
            }
            throw new LaaSCoreRuntimeException(sb.toString());
        }
        if (fieldFormatList == null)
            throw new InputFormatNotFoundException("InputFormat not found");
        if (lineSplitRegex == null)
            throw new Regex4LineSplitNotFoundException("Regex not found for " + line.getContent());
        String test = line.getContent();
        String[] fieldContents = RegexUtil.getValues(line.getContent(), lineSplitRegex);
        Collection<Field> fieldList = new ArrayList<>();

        for (int i = 0; i < fieldContents.length; i++) {
            Field f = null;
            FieldFormat ff = fieldFormatList.get(i);
            String fieldFormatType = ff.getType();

            if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_STRING)) {
                f = new TextField(ff.getName(), fieldContents[i]);
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_INTEGER)) {
                f = new IntegerField(ff.getName(), fieldContents[i]);
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_DATETIME)) {
                f = new DateTimeField(ff.getName(), fieldContents[i], ff.getDateFormat());
            } else if (fieldFormatType.equals(Constants.FIELD_FORMAT_TYPE_DOUBLE)) {
                f = new DoubleField(ff.getName(), fieldContents[i]);
            }

            if (ff.isSortable() && (line instanceof LogLine)) {

                ((LogLine) line).setSortedField(f);
            }
            fieldList.add(f);
        }
        return new BasicSplitResult(fieldList);
    }

    private Map<String, LineAttributes> getFormatDataFromJsonFile(File formatFile) {
        Map<String, LineAttributes> lineAttrMap = new HashMap<>();
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
