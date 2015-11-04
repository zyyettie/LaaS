package org.g6.laas.core.log.unit;

import lombok.Data;
import org.g6.laas.core.log.line.Line;

@Data
public abstract class SingleLineUnit implements IUnit {
    private Line line;

    public SingleLineUnit(Line line) {
        this.line = line;
    }

    @Override
    public String getContent() {
        return line.toString()+"\n";
    }

    @Override
    public String getHtmlContent() {
        return getContent();
    }
}
