package org.g6.laas.core.format.json;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;

import java.util.List;

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
}
