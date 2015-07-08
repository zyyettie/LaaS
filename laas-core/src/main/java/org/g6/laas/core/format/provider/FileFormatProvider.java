package org.g6.laas.core.format.provider;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.format.InputFormat;

import java.io.File;

@Data
@NoArgsConstructor
public abstract class FileFormatProvider implements FormatProvider {

    private File file;

    public FileFormatProvider(File file) {
        if (file == null || !file.exists())
            throw new IllegalArgumentException("must provide file containing format definition.");
        this.file = file;
    }

    public FileFormatProvider(String file) {
        this(new File(file));
    }

    @Override
    public InputFormat getInputFormat() {
        return parse();
    }

    protected abstract InputFormat parse();

}
