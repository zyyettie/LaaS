package org.g6.laas.sm.log.unit;

import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.unit.SingleLineUnit;

public class SMRadLineUnit extends SingleLineUnit{
    private String radName;
    private String panelName;
    private String panelType;

    public SMRadLineUnit(Line line) {
        super(line);
    }

    public String getRadName() {
        return radName;
    }

    public void setRadName(String radName) {
        this.radName = radName;
    }

    public String getPanelName() {
        return panelName;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
    }

    public String getPanelType() {
        return panelType;
    }

    public void setPanelType(String panelType) {
        this.panelType = panelType;
    }

    @Override
    public String getContent() {
        return radName+"\t"+panelName+"\t"+panelType+"\n";
    }
}
