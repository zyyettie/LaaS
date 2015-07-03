package org.g6.util;

public class Constants {
    public static final int LOG_TYPE_FILE = 0;
    public static final int LOG_TYPE_HDFS_FILE = 1;
    public static final String REGEX_PREFIX="REGEX:";
    public static final String FIELD_FORMAT_TYPE_STRING="String:";
    public static final String FIELD_FORMAT_TYPE_DOUBLE="Double:";
    public static final String FIELD_FORMAT_TYPE_DATETIME="DateTime:";
    public static final String FIELD_FORMAT_TYPE_INTEGER="Integer:";

    public static final String[] CAUSE_METHOD_NAMES = {
            "getCause",
            "getNextException",
            "getTargetException",
            "getException",
            "getSourceException",
            "getRootCause",
            "getCausedByException",
            "getNested",
            "getLinkedException",
            "getNestedException",
            "getLinkedCause",
            "getThrowable",
    };
}
