package org.g6.laas.core.format.provider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.cache.InputFormatCache;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

@Slf4j
@Data
public final class DefaultFormatProvider extends FileFormatProvider {
    private String key;
    @Autowired
    private InputFormatCache cache;

    public DefaultFormatProvider(String key) {
        this.key = key;
    }

    @Override
    protected InputFormat parse() {
       // DefaultInputFormat result = new DefaultInputFormat();

        return null;
    }
}
