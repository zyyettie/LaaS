package org.g6.laas.core.log.line;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.log.result.SplitResult;

@Data
@NoArgsConstructor
public abstract class Line implements ILine,Comparable {
    private ILogFile file;
    private String content;
    private int lineNumber;
    private InputFormat inputFormat = null;

    public Line(ILogFile file, String content, int lineNumber, InputFormat inputFormat) {
        this.file = file;
        this.content = content;
        this.lineNumber = lineNumber;
        this.inputFormat = inputFormat;
    }

    public abstract SplitResult split();

    @Override
    public String toString() {
        return "File=" + file.getPath() + ";line=" + lineNumber + ";content=" + content;
    }
}
