package org.g6.laas.core.format.json;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.FieldFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.LogFieldFormat;
import org.g6.util.Constants;
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class JSONClient {

    /**
     * @param jsonFile json file, need to use relative path here e.g./org/g6/xxx.json
     * @return
     */
    public static JSONFile<JSONLine> getFormatFromJSON(String jsonFile) {
        List<String> list = FileUtil.readFile(FileUtil.getRelativeInputStream(jsonFile));
        String jsonStr = "";
        for (String str : list) {
            jsonStr += str.trim();
        }

        log.debug(jsonStr);

        JSONFile<JSONLine> jsonObj = JSONUtil.fromJson(jsonStr, new TypeToken<JSONFile<JSONLine>>() {
        }.getType());

        return jsonObj;
    }

    public static void getInputFormat(JSONFile<JSONLine> jsonFile, InputFormat format) {
        String dateFormat = jsonFile.getDateTimeFormat();
        List<JSONLine> jsonLines = jsonFile.getLines();

        Map<String, List<FieldFormat>> fieldFormatMap = new HashMap<>();
        Map<String, String> regexMap = new HashMap<>();

        for(JSONLine line : jsonLines){
            String key = line.getKey();
            String regex = line.getRegex();
            regexMap.put(key, regex);

            List<LogFieldFormat> fields = line.getFields();
            List<FieldFormat> tempFields = new ArrayList<>();

            for(LogFieldFormat field : fields){
                if(field.getType().equals(Constants.FIELD_FORMAT_TYPE_DATETIME)){
                    field.setDateFormat(dateFormat);
                }
                tempFields.add(field);
            }
            fieldFormatMap.put(key, tempFields);
        }

        format.setLineFormatsByKey(fieldFormatMap);
        format.setRegex4LineSplit(regexMap);
    }

}
