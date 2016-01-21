package org.g6.bigdata.log;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogLine implements Comparable<LogLine>{
    private String content;
    private String fileName;
    //The number may not be required. for big file,
    //the blocks will be in different machines. The calculation will be done based on all blocks
    private String number;
    private double sortValue;

    public LogLine(String content, String fileName){
        this.content = content;
        this.fileName = fileName;
    }

    @Override
    public int compareTo(LogLine o) {
        if(this.getSortValue() == o.getSortValue()) return 0;
        return this.getSortValue() < o.getSortValue() ? -1 : 1;
    }
}
