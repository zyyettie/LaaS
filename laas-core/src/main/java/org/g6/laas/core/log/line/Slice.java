package org.g6.laas.core.log.line;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/*
this class existing for consideration sometimes we need read multiple lines that is as slice
including [start, end) content
 */

@Data
@NoArgsConstructor
public abstract class Slice implements ILine {
    private int start;
    private int end;
    private Collection<ILine> lines;

    public Slice(Collection<ILine> lines) {
        this.lines = lines;
    }

    public void addLine(ILine line) {
        lines.add(line);
    }

    public Collection<ILine> getLines() {
        return lines;
    }
}
