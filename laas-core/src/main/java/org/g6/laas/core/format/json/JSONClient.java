package org.g6.laas.core.format.json;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.g6.util.FileUtil;
import org.g6.util.JSONUtil;

import java.util.List;

@Slf4j
public class JSONClient {

    public static JSONFile<JSONLine> main(String jsonfile) {
        List<String> list = FileUtil.readFile(FileUtil.getRelativeInputStream("/sm_rte_log.json"));
        String jsonStr = "";
        for(String str: list){
            jsonStr += str.trim();
        }

        System.out.print(jsonStr);


        JSONFile<JSONLine> common = new JSONFile();

        JSONFile<JSONLine> jsonObj = JSONUtil.fromJson(jsonStr, new TypeToken<JSONFile<JSONLine>>() {}.getType());

         return jsonObj;
    }
}
