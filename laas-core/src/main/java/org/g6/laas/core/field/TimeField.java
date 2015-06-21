package org.g6.laas.core.field;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: jili
 * Date: 6/21/15
 * Time: 4:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimeField extends AbstractField<Date>{
    @Override
    public Date getValue() {
        return null;
    }

    public TimeField(String content){
        super(content);
    }
}
