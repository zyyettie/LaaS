package org.g6.laas.core.format;

import org.g6.util.Constants;

import java.util.List;
import java.util.Map;

public class SMRTELogInputFormat implements InputFormat {
    @Override
    public boolean isDiff() {
        return true;
    }

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public String getSeperator() {
        return Constants.SEPERATOR_SPACE;
    }

    @Override
    public String[] getDefaultFormat() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public String[] getFormat() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    @Override
    public Map<String[], List<FieldFormat>> getFormats() {


        return null;
    }
}
