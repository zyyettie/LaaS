package org.g6.laas.core.log;

import lombok.Data;
import org.g6.laas.core.format.FieldFormat;

import java.util.List;

@Data
public class LineAttributes  {
    String splitRegex;
    List<FieldFormat> fieldFormats;
}
