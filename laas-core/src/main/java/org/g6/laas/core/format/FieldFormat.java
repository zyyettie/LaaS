package org.g6.laas.core.format;

import java.io.Serializable;

public interface FieldFormat extends Serializable {
    String getName();

    String getType();

    String getDateFormat();

    boolean isSortable();
}
