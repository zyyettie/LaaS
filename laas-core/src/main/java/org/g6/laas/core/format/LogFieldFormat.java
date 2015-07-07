package org.g6.laas.core.format;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogFieldFormat implements FieldFormat {
    private String name;
    private String type;
    @SerializedName("date_time_format")
    private String dateFormat;
    private boolean sortable;

    public LogFieldFormat(String name, String type) {
         this(name, type, null);
    }
    public LogFieldFormat(String name, String type, String dateFormat) {
        this(name, type, dateFormat, false);
    }

    public LogFieldFormat(String name, String type, boolean sortable){
        this(name, type, null, sortable);
    }
}
