package org.g6.laas.core.format.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import org.g6.laas.core.format.LogFieldFormat;

import java.util.List;

@Data
public class JSONLineFormat {
    private String key;
    @SerializedName("line_split_regex")
    private String regex;
    private List<LogFieldFormat> fields;
}
