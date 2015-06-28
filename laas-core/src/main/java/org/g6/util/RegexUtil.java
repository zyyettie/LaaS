package org.g6.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    public static String[] getValues(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str);
        String[] values = null;

        if (matcher.find()) {
            int gc = matcher.groupCount();
            values = new String[gc - 1];

            for (int i = 1; i <= gc; i++) {
                values[i] = matcher.group(i);
            }
        }
        return values;
    }

    public static boolean isMatched(String str, String regex){
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(str);

        return matcher.matches();
    }

}
