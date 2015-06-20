package org.g6.laas.core.log;

import org.g6.laas.core.field.Field;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.InputFormat;

import java.util.Collection;

public class LogLine extends Line {
    public LogLine() {
    }

    public LogLine(ILogFile file, String content, int lineNumber) {
        super(file, content, lineNumber, null);
    }

    public LogLine(ILogFile file, String content, int lineNumber, InputFormat format) {
        super(file, content, lineNumber, format);
    }

    @Override
    public Collection<Field> split() {
        if (isSplitable()) {

        }

        return null;
    }
}
