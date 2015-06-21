package org.g6.laas.core.log;

import lombok.NoArgsConstructor;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.InputFormat;

@NoArgsConstructor
public abstract class Line extends Slice implements Comparable{

    public Line(ILogFile file, String content, int lineNumber, InputFormat format) {
        super(file, content, lineNumber, lineNumber + 1, format);
    }

    public int getLineNumber() {
        return getStart();
    }
}
