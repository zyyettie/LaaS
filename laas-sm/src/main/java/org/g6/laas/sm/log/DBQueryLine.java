package org.g6.laas.sm.log;

import org.g6.laas.core.field.Field;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.log.Line;

import java.util.Collection;

public class DBQueryLine extends Line {
    public DBQueryLine(ILogFile file, String content, int lineNumber, InputFormat inputFormat) {
        super(file, content, lineNumber, inputFormat);
    }

    public DBQueryLine(Line line){
        this(line.getFile(),line.getContent(),line.getLineNumber(),line.getInputFormat());
    }

    @Override
    public Collection<Field> split() {
        return null;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
