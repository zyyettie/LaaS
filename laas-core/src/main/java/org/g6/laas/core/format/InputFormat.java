package org.g6.laas.core.format;

import java.util.List;
import java.util.Map;

public interface InputFormat {
    boolean isDiff();//For RTE, the line format is different.
    boolean isDefault();//normally customer will not change the format of log file
    String getSeperator(); // it is space in most of scenarios
    String[] getDefaultFormat() throws IllegalAccessException;
    String[] getFormat() throws IllegalAccessException;
    // is it possible to have more than one key word?
    // Here is to assume the line can be recognized by some key words
    Map<String, List<FieldFormat>> getFormats();
    //if some lines should be ignored e.g. SOAP message
}
