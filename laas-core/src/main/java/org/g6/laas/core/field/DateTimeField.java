package org.g6.laas.core.field;

import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.util.DateTimeUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Note datePattern must be there. That is to say whenever a date string comes the matched date format
 * must be there.
 */
public class DateTimeField extends AbstractField<Date> {
    private Date date;

    public DateTimeField(String name, String content, String datePattern) {
        this(name, content, datePattern, null, null);
    }

    public DateTimeField(String name, String content, String datePattern, String timezoneID) {
        this(name, content, datePattern, timezoneID, null);
    }

    public DateTimeField(String name, String content, String datePattern, Locale locale) {
        this(name,content, datePattern, null, locale);
    }

    public DateTimeField(String name, String content, String datePattern, String timezoneID, Locale locale) {
        super(name, content);

        try {
            this.date = DateTimeUtil.parseAsDate(content,datePattern, timezoneID, locale);
        } catch (ParseException e) {
            throw new LaaSCoreRuntimeException("Exception is thrown when parsing "
                    + content
                    + "with the format "
                    + datePattern, e);
        }
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