package org.g6.laas.core.format.json;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JSONFile<T> implements Serializable {
    @SerializedName("file_name")
    String fileName;
    @SerializedName("date_time_format")
    String dateTimeFormat;

    private List<T> lines;

}
