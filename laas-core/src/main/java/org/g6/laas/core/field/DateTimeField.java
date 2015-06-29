package org.g6.laas.core.field;

import java.util.Date;

public class DateTimeField extends AbstractField<Date>{
    @Override
    public Date getValue() {
        return null;
    }

    public DateTimeField(String content){
        super(content);
    }

    @Override
    public int compareTo(Field o) {
        return getValue().compareTo((Date)o);
    }
}
