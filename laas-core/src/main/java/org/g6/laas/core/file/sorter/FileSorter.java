package org.g6.laas.core.file.sorter;

import org.g6.laas.core.file.ILogFile;

import java.util.List;

public interface FileSorter {
    void sort(List<ILogFile> files, SortOrder order);
    void sort(List<ILogFile> files);
}
