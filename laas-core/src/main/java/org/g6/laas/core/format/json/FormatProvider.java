package org.g6.laas.core.format.json;

import org.g6.laas.core.format.InputFormat;
import org.g6.util.FileUtil;

import java.util.List;

public interface FormatProvider {
    InputFormat getInputFormat();
}
