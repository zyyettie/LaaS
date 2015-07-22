package org.g6.laas.core.log.unit;

import org.g6.laas.core.log.line.Line;

public abstract class SingleLineUnit implements IUnit {
    private Line line;

    public SingleLineUnit(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    @Override
    public String getContent() {
        return line.toString()+"\n";
    }
}
