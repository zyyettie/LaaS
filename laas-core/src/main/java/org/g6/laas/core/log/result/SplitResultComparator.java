package org.g6.laas.core.log.result;

import org.g6.laas.core.log.line.Line;

import java.util.Comparator;

public class SplitResultComparator implements Comparator<SplitResult> {

    @Override
    public int compare(SplitResult o1, SplitResult o2) {
        Line line1 = o1.getLine();
        Line line2 = o2.getLine();
        return line1.compareTo(line2);
    }
}
