package org.g6.laas.core.log.result;

import org.g6.laas.core.field.Field;
import org.g6.laas.core.log.line.Line;

import java.util.Collection;

public interface SplitResult {

    Field get(String name);

    Field get(int index);

    int size();

    Collection<Field> getAll();

    Line getLine();

    void setLine(Line line);

}
