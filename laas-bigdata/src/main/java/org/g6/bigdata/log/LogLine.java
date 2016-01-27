package org.g6.bigdata.log;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.g6.laas.core.field.Field;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class LogLine implements Serializable, Comparable<LogLine>{
    private String content;
    private Field sortedField = null;

    @Override
    public int compareTo(LogLine line) {
        return sortedField.compareTo(line.sortedField);
    }
}
