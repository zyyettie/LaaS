package org.g6.laas;

import lombok.Data;
import org.g6.laas.core.format.InputFormat;

@Data
public class LaaSContext {
    //Note we assume the same types of files are analyzed.
    //but different types of log files could be analyzed in the future.
    //if so, here should be a list which contains different input formats
    InputFormat inputFormat;
}
