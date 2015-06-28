package org.g6.laas.core.log;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.InputFormat;

import java.util.Collection;

@Data
@NoArgsConstructor
public abstract class Line implements Comparable {
    private ILogFile file;
    private String content;
    private int lineNumber;
    private InputFormat inputFormat = null;
    private boolean splitNeeded;

    public Line(ILogFile file, String content, int lineNumber, InputFormat inputFormat) {
        this.file = file;
        this.content = content;
        this.lineNumber = lineNumber;
        this.inputFormat = inputFormat;
        if (inputFormat != null) {
            setSplitNeeded(true);
        }
    }

    public abstract Collection<Field> split();
}
