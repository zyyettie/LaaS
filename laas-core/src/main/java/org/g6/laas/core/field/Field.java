package org.g6.laas.core.field;

public interface Field<T> extends Comparable<Field> {

    String getContent();

    String getName();

    T getValue();

}
