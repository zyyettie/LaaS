package org.g6.laas.sm.log.unit;

import lombok.Data;
import org.g6.laas.core.log.unit.IUnit;
import org.g6.laas.core.log.unit.LineSetUnit;

import java.util.Collection;

@Data
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

    @Override
    public String getHtmlContent() {
        StringBuilder prefix = new StringBuilder();
        StringBuilder prefix2 = new StringBuilder();
        for (int i=0; i<level-1; i++) {
            prefix2.append("  ");
        }
        if (level > 0) {
            prefix.append(prefix2).append("  ");
        }

        StringBuilder content = new StringBuilder();
        if (radName !=null && radName.length()>0) {
            if (level > 0) {
                content.append("<li>");
            }
            content.append(radName).append("\n").append(prefix).append("<ul>").append("\n");
        }

        for (IUnit unit : set) {
            content.append(prefix).append(unit.getHtmlContent());
        }

        if (radName !=null && radName.length()>0) {
            content.append(prefix).append("</ul>\n");
            if (level > 0) {
                content.append(prefix2).append("</li>");
            }
            content.append("\n");
        }

        return content.toString();
    }
}
