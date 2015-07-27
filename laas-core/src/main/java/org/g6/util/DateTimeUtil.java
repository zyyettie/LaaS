package org.g6.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeUtil {

    public static final String DEFAULT_TIMEZONE_ID = "GMT";
    public static final String DEFAULT_DATE_PATTERN = "MM/dd/yy HH:mm:ss"; // "yyyy-MM-dd HH:mm:ss"?
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH; // Locale.getDefault(Locale.Category.FORMAT)?

    public enum DiffType {
       MILLISECOND, SECOND, MINUTE, HOUR, DAY;
    }

    public static DateFormat getDateFormat(String datePattern, String timezoneID, Locale locale) {
        String _datePattern;
        if (datePattern == null || datePattern.equals(""))
            _datePattern = DEFAULT_DATE_PATTERN;
        else
            _datePattern = datePattern;
        String _timezoneID;
        if (timezoneID == null || timezoneID.equals(""))
            _timezoneID = DEFAULT_TIMEZONE_ID;
        else
            _timezoneID = timezoneID;
        Locale _locale;
        if (locale == null)
            _locale = DEFAULT_LOCALE;
        else
            _locale = locale;
        DateFormat formatter = new SimpleDateFormat(_datePattern, _locale);
        formatter.setTimeZone(TimeZone.getTimeZone(_timezoneID));
        return formatter;
    }

    public static Date parseAsDate(String s) throws ParseException {
        DateFormat formatter = getDateFormat(null, null, null);
        return formatter.parse(s);
    }

    public static Date parseAsDate(String s, String datePattern) throws ParseException {
        DateFormat formatter = getDateFormat(datePattern, null, null);
        return formatter.parse(s);
    }

    public static Date parseAsDate(String s, String datePattern, String timezoneID) throws ParseException {
        DateFormat formatter = getDateFormat(datePattern, timezoneID, null);
        return formatter.parse(s);
    }

    public static Date parseAsDate(String s, String datePattern, String timezoneID, Locale locale) throws ParseException {
        DateFormat formatter = getDateFormat(datePattern, timezoneID, locale);
        return formatter.parse(s);
    }

    public static String formatAsString(Date date, String datePattern) throws ParseException {
        return getDateFormat(datePattern, null, null).format(date);
    }

    public static String getStringByDiff(String string, String datePattern, long diffNum, DiffType diffType) throws ParseException {
        Date d1 = getDateByDiff(string, datePattern, diffNum, diffType);
        return formatAsString(d1, datePattern);
    }

    public static Date getDateByDiff(String string, String datePattern, long diffNum, DiffType diffType) throws ParseException {
        Date d1 = parseAsDate(string, datePattern);
        d1 = getDateByDiff(d1, diffNum, diffType);
        return d1;
    }

    public static Date getDateByDiff(Date date, long diffNum, DiffType diffType) {
        Date d1 = new Date();
        d1.setTime(date.getTime());
        switch (diffType) {
            case MILLISECOND: d1.setTime(d1.getTime()+diffNum);
            case SECOND: d1.setTime(d1.getTime()+diffNum*1000);
            case MINUTE: d1.setTime(d1.getTime()+diffNum*1000*60);
            case HOUR: d1.setTime(d1.getTime()+diffNum*1000*60*60);
            case DAY: d1.setTime(d1.getTime()+diffNum*1000*60*60*24);
            default: ;
        }
        return d1;
    }

    public static long getDateDiff(String string1, String string2, String datePattern, DiffType diffType) throws ParseException {
        long d1 = parseAsDate(string1, datePattern).getTime();
        long d2 = parseAsDate(string2, datePattern).getTime();
        switch (diffType) {
            case MILLISECOND: return d1-d2;
            case SECOND: return (d1-d2)/(1000);
            case MINUTE: return (d1-d2)/(1000*60);
            case HOUR: return (d1-d2)/(1000*60*60);
            case DAY: return (d1-d2)/(1000*60*60*24);
            default: return 0;
        }
    }

    public static long getDateDiff(Date date1, Date date2, DiffType diffType) {
        long d1=date1.getTime();
        long d2=date2.getTime();
        switch (diffType) {
            case MILLISECOND: return d1-d2;
            case SECOND: return (d1-d2)/(1000);
            case MINUTE: return (d1-d2)/(1000*60);
            case HOUR: return (d1-d2)/(1000*60*60);
            case DAY: return (d1-d2)/(1000*60*60*24);
            default: return 0;
        }
    }

    public void main (String args[]) {
        try {
            System.out.println(getStringByDiff("2015-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss", 1, DiffType.DAY));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
