package org.g6.laas.sm.log.unit;

import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.unit.SingleLineUnit;

public class SMJSLineUnit extends SingleLineUnit {
    private String jsName;

    public SMJSLineUnit(Line line) {
        super(line);
    }

    public String getJsName() {
        return jsName;
    }

    public void setJsName(String jsName) {
        this.jsName = jsName;
    }

    @Override
    public String getContent() {
        return "JS: "+jsName+"\n";
    }
}
