package org.g6.laas.core.format.provider;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.cache.FileFormatCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@NoArgsConstructor
@Data
@Service
public class DefaultInputFormatProvider extends FileFormatProvider {
    @Autowired
    FileFormatCache cache;
    private String formatKey;

    public DefaultInputFormatProvider(String  formatKey) {
        this.formatKey = formatKey;
    }

    @Override
    protected InputFormat parse() {
        DefaultInputFormat inputFormat = new DefaultInputFormat();
        if(cache == null)
            cache = new FileFormatCache();
        inputFormat.setLineAttrMap(cache.getFileFormat(formatKey));

        return inputFormat;
    }


}
