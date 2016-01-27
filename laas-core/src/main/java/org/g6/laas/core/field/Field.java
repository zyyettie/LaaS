package org.g6.laas.core.field;

import java.io.Serializable;

public interface Field<T> extends Serializable, Comparable<Field> {

    String getContent();

    String getName();

    T getValue();

}
