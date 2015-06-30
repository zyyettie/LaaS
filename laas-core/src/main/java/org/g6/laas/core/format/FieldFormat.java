package org.g6.laas.core.format;

public interface FieldFormat {
    String getName();
    String getType();
    String getDateFormat();
    boolean isSortable();
}
