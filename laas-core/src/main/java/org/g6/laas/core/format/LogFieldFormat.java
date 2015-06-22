package org.g6.laas.core.format;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFieldFormat implements FieldFormat {
    private String name;
    private String value;
    private boolean sortable;
    private boolean splitable;
    private String separator;
    private int indexOfValue;
    private boolean relatedToPrev; //This is most for Date and Time fields
    private boolean relatedToNext;

    public LogFieldFormat(String name, String value) {
         this(name, value, false, false);
    }

    public LogFieldFormat(String name, String value, boolean relatedToPrev, boolean relatedToNext) {
         this(name, value, false, false, "", 0, relatedToPrev, relatedToNext);
    }

    public LogFieldFormat(String name, String value, boolean sortable) {
         this(name, value, sortable, false, "", 0);
    }

    public LogFieldFormat(String name, String value, boolean sortable, boolean splitable, String separator, int indexOfValue) {
        this(name, value, sortable, splitable, separator, indexOfValue, false, false);
    }
}
