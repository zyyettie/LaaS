package org.g6.laas.core.log.line;

import lombok.Data;
import org.g6.laas.core.format.FieldFormat;

import java.io.Serializable;
import java.util.List;

@Data
public class LineAttributes implements Serializable {
    private String name;
    private String key;
    private String splitRegex;
    private List<FieldFormat> fieldFormats;
}
