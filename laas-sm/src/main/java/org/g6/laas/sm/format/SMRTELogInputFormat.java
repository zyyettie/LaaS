package org.g6.laas.sm.format;

import org.g6.laas.core.format.FieldFormat;
import org.g6.laas.core.format.InputFormat;
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
    public Map<String, List<FieldFormat>> getLineFormatsByKey() {
         //TODO the keyword parts will be from property file or database. now it can be hardcoded.
        return null;
    }

    @Override
    public Map<String, String> getRegex4LineSplit() {
        return null;
    }
}
