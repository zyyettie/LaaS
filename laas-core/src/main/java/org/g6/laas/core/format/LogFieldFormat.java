package org.g6.laas.core.format;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFieldFormat implements FieldFormat {
    private String name;
    private String type;
    private boolean sortable;

    public LogFieldFormat(String name, String type) {
         this(name, type, false);
    }
}
