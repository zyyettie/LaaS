package org.g6.laas.core.format;

public interface FieldFormat {
    String getName();
    boolean isRelatedToPrev();
    boolean isRelatedToNext();
    String getValue();
}
