package org.g6.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ExceptionUtils {

    public static String getRootCauseMessage(final Throwable t) {
        Throwable root = ExceptionUtils.getRootCause(t);
        root = root == null ? t : root;
        return root.getMessage();
    }


    public static Throwable getRootCause(final Throwable throwable) {
        final List<Throwable> list = getThrowableList(throwable);
        return list.size() < 2 ? null : list.get(list.size() - 1);
    }


    public static List<Throwable> getThrowableList(Throwable throwable) {
        final List<Throwable> list = new ArrayList<Throwable>();
        while (throwable != null && list.contains(throwable) == false) {
            list.add(throwable);
            throwable = ExceptionUtils.getEXCause(throwable);
        }
        return list;
    }


    public static Throwable getEXCause(final Throwable throwable) {
        return getEXCause(throwable, Constants.CAUSE_METHOD_NAMES);
    }

    public static Throwable getEXCause(final Throwable throwable, String[] methodNames) {
        if (throwable == null) {
            return null;
        }

        if (methodNames == null) {
            methodNames = Constants.CAUSE_METHOD_NAMES;
        }

        for (final String methodName : methodNames) {
            if (methodName != null) {
                final Throwable cause = getCauseWithMethodName(throwable, methodName);
                if (cause != null) {
                    return cause;
                }
            }
        }

        return null;
    }

    private static Throwable getCauseWithMethodName(final Throwable throwable, final String methodName) {
        Method method = null;
        try {
            method = throwable.getClass().getMethod(methodName);
        } catch (final NoSuchMethodException ignored) {
            // do nothing
        } catch (final SecurityException ignored) {
            // do nothing
        }

        if (method != null && Throwable.class.isAssignableFrom(method.getReturnType())) {
            try {
                return (Throwable) method.invoke(throwable);
            } catch (final IllegalAccessException ignored) {
                // do nothing
            } catch (final IllegalArgumentException ignored) {
                // do nothing
            } catch (final InvocationTargetException ignored) {
                // do nothing
            }
        }
        return null;
    }

}
