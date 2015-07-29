package org.g6.laas.sm.log.unit;

import org.g6.laas.core.log.unit.IUnit;
import org.g6.laas.core.log.unit.LineSetUnit;

import java.util.Collection;

public class SMRadLineSetUnit extends LineSetUnit {
    private String radName;
    private int processId = -1;
    private int threadId = -1;

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

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    @Override
    public String getContent() {
        StringBuilder content = new StringBuilder();
        if (radName !=null && radName.length()>0) {
            content.append("calling ").append(radName).append("\n");
        }
        for (IUnit unit : set) {
            for (int i=0; i<level; i++) {
                content.append("  ");
            }
            content.append(unit.getContent());
        }

        return content.toString();
    }
}
