package org.g6.laas.core.format;

import java.util.List;
import java.util.Map;

public interface InputFormat {
    boolean isDiff();
    void setDiff(boolean diff);
    boolean isDefaultFormat();
    void setDefaultFormat(boolean df);
    String[] getDefaultFormats() throws IllegalAccessException;
    void setDefaultFormats(String[] defaultFormats);
    String[] getFormats() throws IllegalAccessException;
    void setFormats(String[] formats);
    Map<String, List<FieldFormat>> getLineFormatsByKey();
    void setLineFormatsByKey(Map<String, List<LogFieldFormat>> lineFormats);
    Map<String, String> getRegex4LineSplit();
    void setRegex4LineSplit(Map<String, String> regex);
}
