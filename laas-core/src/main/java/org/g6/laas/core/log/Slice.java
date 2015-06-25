package org.g6.laas.core.log;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.InputFormat;

import java.util.Collection;

/*
this class existing for consideration sometimes we need read multiple lines that is as slice
including [start, end) content
 */

@Data
@NoArgsConstructor
public abstract class Slice {
    private String content;
    private int start;
    private int end;
    private Collection<Line> lines;

    public Slice(Collection<Line> lines) {
        this.lines = lines;
    }

    void addLine(Line line){
        lines.add(line);
    }
}
