package org.g6.util;

import org.g6.laas.core.exception.LaaSExceptionHandler;

import java.lang.reflect.Method;

public class JUnitUtil {

    public static Object getTestResult(Object obj, String method, Class[] args, Object[] values) {
        Object rtnObj = null;
        try {
            Method m = obj.getClass().getDeclaredMethod(method, args);
            m.setAccessible(true);
            rtnObj = m.invoke(obj, values);
            m.setAccessible(false);
        } catch (Exception e) {
            LaaSExceptionHandler.handleException("Error happens when calling the " + method + " method of " + obj.getClass().getName(), e);
        }

        return rtnObj;
    }

    public static Object getTestResult(Object obj, String method) {
        return getTestResult(obj, method, null, null);
    }

}
