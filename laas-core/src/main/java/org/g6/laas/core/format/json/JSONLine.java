package org.g6.laas.core.format.json;

import lombok.Data;
import org.g6.laas.core.format.LogFieldFormat;

import java.util.List;

@Data
public class JSONLine {
    private String key;
    private String regex;
    private List<LogFieldFormat> fields;
}
