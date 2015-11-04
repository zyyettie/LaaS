package org.g6.laas.sm.log.unit;

import lombok.Data;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.unit.SingleLineUnit;

@Data
public class SMRadLineUnit extends SingleLineUnit{
    private String radName;
    private String panelName;
    private String panelType;

    public SMRadLineUnit(Line line) {
        super(line);
    }

    @Override
    public String getContent() {
        return radName+"\t"+panelName+"\t"+panelType+"\n";
    }

    @Override
     public String getHtmlContent() {
        return "<li>"+radName+"\t"+panelName+"\t"+panelType+"</li>\n";
    }
}
