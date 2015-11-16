package org.g6.laas.core.log.unit;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;

@Data
public abstract class LineSetUnit implements IUnit {
    protected Collection<IUnit> set;
    protected int level;

    public LineSetUnit() {
        this.set = new ArrayList<>();
        this.level = 0;
    }

    public LineSetUnit(Collection<IUnit> set) {
        this(set, 0);
    }

    public LineSetUnit(Collection<IUnit> set, int level) {
        this.set = set;
        this.level = level;
    }

    public void addUnit(IUnit unit) {
        this.set.add(unit);
    }

    @Override
    public String getContent() {
        String content = "";
        for (IUnit unit:set) {
            content+=unit.getContent();
        }

        return content;
    }

    @Override
    public String getHtmlContent() {
        return getContent();
    }

    @Override
    public String getJsonContent() {
        return getContent();
    }
}
