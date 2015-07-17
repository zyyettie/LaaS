package org.g6.laas.core.file.sorter;

import org.g6.laas.core.file.ILogFile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractLogFileSorter implements FileSorter<ILogFile> {

    protected abstract Comparator<ILogFile> getComparator();

    public void sort(List<ILogFile> files, SortOrder order) {
        Comparator<ILogFile> comparator = getComparator();
        Collections.sort(files, comparator);
        if (order.equals(SortOrder.DESC))
            Collections.reverse(files);
    }

    @Override
    public void sort(List<ILogFile> files) {
        sort(files, SortOrder.ASC);
    }
}
