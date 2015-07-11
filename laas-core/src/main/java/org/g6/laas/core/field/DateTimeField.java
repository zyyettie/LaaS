package org.g6.laas.core.field;

import org.g6.laas.core.exception.LaaSCoreRuntimeException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Note datePattern must be there. That is to say whenever a date string comes the matched date format
 * must be there.
 */
public class DateTimeField extends AbstractField<Date> {
    private Date date;
    private String timezoneID = "GMT";
    private String datePattern = "MM/dd/yyyy HH:mm:ss"; // Default date format in SM - 06/20/2015 17:05:22;
    private Locale locale = Locale.ENGLISH;

    public DateTimeField(String content, String datePattern) {
        this(content, datePattern, null, null);
    }

    public DateTimeField(String content, String datePattern, Locale locale) {
        this(content, datePattern, locale, null);
    }

    public DateTimeField(String content, String datePattern, String timezoneID) {
        this(content, datePattern, null, timezoneID);
    }

    public DateTimeField(String content, String datePattern, Locale locale, String timezoneID) {
        super(content);
        if (locale != null)
            this.locale = locale;
        if (null != timezoneID && !timezoneID.equals(""))
            this.timezoneID = timezoneID;
        this.datePattern = datePattern;

        try {
            parse(content);
        } catch (ParseException e) {
            throw new LaaSCoreRuntimeException("Exception is thrown when parsing "
                    + content
                    + "with the format "
                    + datePattern, e);
        }
    }

    private void parse(String s) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(this.datePattern, this.locale);
        formatter.setTimeZone(TimeZone.getTimeZone(timezoneID));
        this.date = formatter.parse(s);
    }


    public Date getValue() {
        return date;
    }

    @Override
    public int compareTo(Field o) {
        if (getValue() == null && o.getValue() != null) {
            return 1; // null date is the biggest one
        } else if (this.date == null && o.getValue() == null) {
            return 0;
        } else {
            return getValue().compareTo((Date) o.getValue());
        }
    }

}