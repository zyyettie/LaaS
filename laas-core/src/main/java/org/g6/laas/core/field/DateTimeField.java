package org.g6.laas.core.field;

import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateTimeField extends AbstractField<Date> {
    private Date date;
    private String timezoneID = "GMT";
    private String datePattern = "mm/dd/yyyy HH:mm:ss"; // Default date format in SM - 06/20/2015 17:05:22;
    private Locale locale = Locale.ENGLISH;

    public DateTimeField(String content) throws ParseException {
        super(content);
        parse(content);
    }

    public DateTimeField(String content, String timezoneID) throws ParseException {
        super(content);
        this.timezoneID = timezoneID;
        parse(content);
    }

    public DateTimeField(String content, String timezoneID, String datePattern) throws ParseException {
        super(content);
        this.timezoneID = timezoneID;
        this.datePattern = datePattern;
        parse(content);
    }

    public DateTimeField(String content, String timezoneID, String datePattern, Locale locale) throws ParseException {
        super(content);
        this.timezoneID = timezoneID;
        this.datePattern = datePattern;
        this.locale = locale;
        parse(content);
    }

    private void parse(String s) throws ParseException {
        DateFormat formatter = new SimpleDateFormat(this.datePattern, this.locale);
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
            return getValue().compareTo((Date)o.getValue());
        }
    }

}