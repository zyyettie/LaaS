package org.g6.laas.core.format;

import lombok.Data;

@Data
public class RegularInputFormat implements InputFormat<String[]> {
    private String[] lineFormat;
    private String[] defaultLineFormat;

    @Override
    public String[] getLineFormat() {
        return lineFormat;
    }
}
