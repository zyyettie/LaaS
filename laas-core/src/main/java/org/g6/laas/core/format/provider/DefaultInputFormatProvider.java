package org.g6.laas.core.format.provider;


import lombok.NoArgsConstructor;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.format.InputFormat;

import java.io.File;

@NoArgsConstructor
public class DefaultInputFormatProvider extends FileFormatProvider {

    public DefaultInputFormatProvider(File file) {
        super(file);
    }

    @Override
    protected InputFormat parse() {
        return new DefaultInputFormat(getFile());
    }


}
