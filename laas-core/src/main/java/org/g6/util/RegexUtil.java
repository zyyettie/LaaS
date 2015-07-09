package org.g6.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    public static String[] getValues(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str.trim());
        String[] values = null;

        if (matcher.find()) {
            int gc = matcher.groupCount();
            values = new String[gc];

            for (int i = 1; i <= gc; i++) {
                values[i - 1] = matcher.group(i);
            }
        }
        return values;
    }

    public static boolean isMatched(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str);

        return matcher.find();
    }

    public static boolean isFullyMatched(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str);

        return matcher.matches();
    }

    public static String getValue(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str);
        String value = null;

        if (matcher.find()) {
            value = matcher.group();
        }
        return value;
    }

}
