package org.g6.laas.core.format;

import java.util.List;
import java.util.Map;

public interface InputFormat {
    boolean isDiff();
    boolean isDefaultFormat();
    String[] getDefaultFormats() throws IllegalAccessException;
    String[] getFormats() throws IllegalAccessException;
    Map<String, List<FieldFormat>> getLineFormatsByKey();
    Map<String, String> getRegex4LineSplit();
}
