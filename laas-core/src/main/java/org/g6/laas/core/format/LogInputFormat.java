package org.g6.laas.core.format;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LogInputFormat implements InputFormat{
    boolean diff;
    boolean defaultFormat;
    String[] defaultFormats;
    String[] formats;
    Map<String, List<LogFieldFormat>> lineFormatsByKey;
    Map<String, String> regex4LineSplit;
}
