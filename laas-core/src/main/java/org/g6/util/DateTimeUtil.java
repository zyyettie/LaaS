package org.g6.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public long getMsecDiff(String string1, String string2, String pattern) throws ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat(pattern);
        long d1=formatter.parse(string1).getTime();
        long d2=formatter.parse(string2).getTime();
        return d1-d2;
    }

    public long getSecondDiff(String string1, String string2, String pattern) throws ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat(pattern);
        long d1=formatter.parse(string1).getTime();
        long d2=formatter.parse(string2).getTime();
        return (d1-d2)/(1000);
    }

    public long getMinuteDiff(String string1, String string2, String pattern) throws ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat(pattern);
        long d1=formatter.parse(string1).getTime();
        long d2=formatter.parse(string2).getTime();
        return (d1-d2)/(1000*60);
    }

    public long getHourDiff(String string1, String string2, String pattern) throws ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat(pattern);
        long d1=formatter.parse(string1).getTime();
        long d2=formatter.parse(string2).getTime();
        return (d1-d2)/(1000*60*60);
    }

    public long getDayDiff(String string1, String string2, String pattern) throws ParseException {
        SimpleDateFormat formatter=new SimpleDateFormat(pattern);
        long d1=formatter.parse(string1).getTime();
        long d2=formatter.parse(string2).getTime();
        return (d1-d2)/(1000*60*60*24);
    }

    public long getMsecDiff(Date date1, Date date2) {
        long d1=date1.getTime();
        long d2=date2.getTime();
        return d1-d2;
    }

    public long getSecondDiff(Date date1, Date date2) {
        long d1=date1.getTime();
        long d2=date2.getTime();
        return (d1-d2)/(1000);
    }

    public long getMinuteDiff(Date date1, Date date2) {
        long d1=date1.getTime();
        long d2=date2.getTime();
        return (d1-d2)/(1000*60);
    }

    public long getHourDiff(Date date1, Date date2) {
        long d1=date1.getTime();
        long d2=date2.getTime();
        return (d1-d2)/(1000*60*60);
    }

    public long getDayDiff(Date date1, Date date2) {
        long d1=date1.getTime();
        long d2=date2.getTime();
        return (d1-d2)/(1000*60*60*24);
    }
}
