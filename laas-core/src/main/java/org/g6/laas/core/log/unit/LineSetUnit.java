package org.g6.laas.core.log.unit;

import java.util.ArrayList;
import java.util.Collection;

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

    public Collection<IUnit> getSet() {
        return set;
    }

    public void setSet(Collection<IUnit> set) {
        this.set = set;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
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
}
