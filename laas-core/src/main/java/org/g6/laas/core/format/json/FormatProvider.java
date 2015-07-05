package org.g6.laas.core.format.json;

import lombok.AllArgsConstructor;
import org.g6.laas.core.format.InputFormat;
import org.g6.util.FileUtil;

import java.util.List;

@AllArgsConstructor
public abstract class FormatProvider {
    private String file;

    public InputFormat getInputFormat() {
        List<String> list = FileUtil.readFile(FileUtil.getRelativeInputStream(file));
        return parse(list);
    }

    abstract  InputFormat parse(List<String> lineList);
}
