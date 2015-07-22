package org.g6.laas.sm.log.unit;

import org.g6.laas.core.log.unit.IUnit;
import org.g6.laas.core.log.unit.LineSetUnit;

import java.util.Collection;

public class SMRadLineSetUnit extends LineSetUnit {
    private String radName;

    public SMRadLineSetUnit() {
        super();
    }

    public SMRadLineSetUnit(Collection<IUnit> set) {
        super(set);
    }

    public SMRadLineSetUnit(Collection<IUnit> set, int level) {
        super(set, level);
    }

    public String getRadName() {
        return radName;
    }

    public void setRadName(String radName) {
        this.radName = radName;
    }

    @Override
    public String getContent() {
        String content = "";
        if (radName !=null && radName.length()>0) {
            content += "Calling "+radName+"\n";
        }
        for (IUnit unit : set) {
            for (int i=0; i<level; i++) {
                content += "  ";
            }
            content += unit.getContent();
        }

        return content;
    }
}
