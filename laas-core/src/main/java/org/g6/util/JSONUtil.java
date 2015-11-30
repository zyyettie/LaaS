package org.g6.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JSONUtil {
    /**
     * Java Object to JSON transformation
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        Gson gson = new Gson();

        return gson.toJson(obj);
    }

    /**
     * json string to Java Object transformation
     *
     * @param str
     * @param type {@code new TypeToken<List<Bean>>(){}.getType()}
     * @return
     */
    public static <T> T fromJson(String str, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }


    /**
     * JSON string to Java Object transformation
     *
     * @param str
     * @param type
     * @return
     */
    public static <T> T fromJson(String str, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(str, type);
    }

    public static Map<String, Object> fromJson(String jsonStr) {
        Map<String, Object> map = fromJson(
                jsonStr, new TypeToken<Map<String, Object>>() {
        }.getType());

        return map;
    }
}
