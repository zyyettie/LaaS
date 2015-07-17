package org.g6.laas.core.file.sorter;

import java.util.List;

public interface FileSorter<T> {
    void sort(List<T> files, SortOrder order);
    void sort(List<T> files);
}
