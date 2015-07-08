package org.g6.laas.core.format;

import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.SplitResult;

public interface InputFormat {
    SplitResult splitLine(Line line);
}
