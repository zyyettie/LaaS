package org.g6.laas.core.log;

import org.g6.laas.core.field.Field;

import java.util.Collection;

public interface SplitResult {

    Field get(String name);

    Field get(int index);

    int size();

    Collection<Field> getAll();

}
