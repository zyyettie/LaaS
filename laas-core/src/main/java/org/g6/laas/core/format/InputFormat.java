package org.g6.laas.core.format;

import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.result.SplitResult;

public interface InputFormat {
    SplitResult getSplits(Line line);
}
