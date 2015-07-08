package org.g6.laas.core.log;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

@Data
@NoArgsConstructor
public class LineComparator implements Comparator<Line> {
    boolean desc = true;

    public LineComparator(boolean desc) {
        this.desc = desc;
    }

    @Override
    public int compare(Line o1, Line o2) {
        return desc ? (-1) * o1.compareTo(o2) : o1.compareTo(o2);
    }
}
