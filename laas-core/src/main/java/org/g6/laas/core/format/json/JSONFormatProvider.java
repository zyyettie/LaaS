package org.g6.laas.core.format.json;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.FieldFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.LogFieldFormat;
import org.g6.laas.core.format.LogInputFormat;
import org.g6.util.Constants;
import org.g6.util.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JSONFormatProvider extends FormatProvider {

    public JSONFormatProvider(String file) {
        super(file);
    }

    @Override
    InputFormat parse(List<String> lineList) {
        String jsonStr = "";
        for (String str : lineList) {
            jsonStr += str.trim();
        }

        log.debug(jsonStr);

        JSONFile<JSONLine> jsonFile = JSONUtil.fromJson(jsonStr, new TypeToken<JSONFile<JSONLine>>() {
        }.getType());

        String dateFormat = jsonFile.getDateTimeFormat();
        List<JSONLine> jsonLines = jsonFile.getLines();

        Map<String, List<FieldFormat>> fieldFormatMap = new HashMap<>();
        Map<String, String> regexMap = new HashMap<>();

        for (JSONLine line : jsonLines) {
            String key = line.getKey();
            String regex = line.getRegex();
            regexMap.put(key, regex);

            List<LogFieldFormat> fields = line.getFields();
            List<FieldFormat> tempFields = new ArrayList<>();

            for (LogFieldFormat field : fields) {
                if (field.getType().equals(Constants.FIELD_FORMAT_TYPE_DATETIME)) {
                    field.setDateFormat(dateFormat);
                }
                tempFields.add(field);
            }
            fieldFormatMap.put(key, tempFields);
        }
        LogInputFormat inputFormat = new LogInputFormat();
        inputFormat.setLineFormatsByKey(fieldFormatMap);
        inputFormat.setRegex4LineSplit(regexMap);

        return inputFormat;
    }
}
