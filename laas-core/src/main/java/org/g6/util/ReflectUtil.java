package org.g6.util;

import java.lang.reflect.Field;

public class ReflectUtil {

    public static Object newInstance(String className) throws Exception {
        return getClass(className).newInstance();
    }

    public static Class getClass(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }

    public static void set(Field field, Object obj, String value) throws IllegalAccessException {
        if (field.getType().getName().equals("int")) {
            field.setInt(obj, Integer.valueOf(value));
        } else if (field.getType().getName().equals("long")) {
            field.setLong(obj, Long.valueOf(value));
        } else if (field.getType().getName().equals("double")) {
            field.setDouble(obj, Double.valueOf(value));
        } else {
            field.set(obj, value);
        }
    }
}
