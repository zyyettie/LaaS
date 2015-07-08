package org.g6.laas.core.format.provider;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.FieldFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.IrregularInputFormat;
import org.g6.laas.core.format.LogFieldFormat;
import org.g6.laas.core.log.LineAttributes;
import org.g6.util.Constants;
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Data
public final class DefaultFormatProvider extends FileFormatProvider {

  public DefaultFormatProvider(File jsonFile) {
    super(jsonFile);
  }

  public DefaultFormatProvider(String jsonFile) {
    super(jsonFile);
  }

  @Override
  protected InputFormat parse() {
    List<String> lineList = FileUtil.readFile(FileUtil.getRelativeInputStream(getFile().getName()));
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

    Map<String, LineAttributes> fieldFormatMap = new HashMap<>();

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
      fieldFormatMap.put(key, lineAttr);
    }
    IrregularInputFormat inputFormat = new IrregularInputFormat();
    inputFormat.setLineFormat(fieldFormatMap);

    return inputFormat;
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
}
