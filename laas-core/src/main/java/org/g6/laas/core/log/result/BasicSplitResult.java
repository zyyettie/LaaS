package org.g6.laas.core.log.result;


import com.google.common.base.Strings;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.log.line.Line;

import java.util.*;

public class BasicSplitResult implements SplitResult {

    private Map<String, Field> byName = new HashMap<>();

    private List<Field> byIndex = new ArrayList<>();

    private Line line = null;

    public BasicSplitResult(Collection<Field> fields) {
        byIndex.addAll(fields);
        for (Field field : fields) {
            if (!Strings.isNullOrEmpty(field.getName()))
                byName.put(field.getName(), field);
        }
    }

    @Override
    public Field get(String name) {
        return byName.get(name);
    }

    @Override
    public Field get(int index) {
        return byIndex.get(index);
    }

    @Override
    public int size() {
        return byIndex.size();
    }

    @Override
    public Collection<Field> getAll() {
        return byIndex;
    }

    @Override
    public Line getLine(){
        return this.line;
    }

    @Override
    public void setLine(Line line){
        this.line = line;
    }

    public int compareTo(Object o){
        return getLine().compareTo(o);
    }
}
