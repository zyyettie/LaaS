package org.g6.laas.sm.log.unit;

import lombok.Data;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.unit.SingleLineUnit;

@Data
public class SMJSLineUnit extends SingleLineUnit {
    private String jsName;

    public SMJSLineUnit(Line line) {
        super(line);
    }

    @Override
    public String getContent() {
        return "JS: "+jsName+"\n";
    }

    @Override
    public String getHtmlContent() {
        return "<li data-jstree='{\"icon\":false}'>"+"JS: "+jsName+"</li>\n";
    }

    @Override
    public String getJsonContent() {
        return "JS: "+jsName;
    }
}
