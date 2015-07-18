package org.g6.laas.core.format.provider;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.format.FileFormatCacheService;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.cache.FileFormatCache;
import org.g6.laas.core.log.line.LineAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@NoArgsConstructor
@Data
@Service
public class DefaultInputFormatProvider extends FileFormatProvider {
    @Autowired
    FileFormatCacheService cacheService;
    private String formatKey;
    private String[] names;

    public DefaultInputFormatProvider(String formatKey) {
        this(formatKey, null);
    }

    public DefaultInputFormatProvider(String formatKey, String[] names) {
        this.formatKey = formatKey;
        this.names = names;
    }

    @Override
    protected InputFormat parse() {
        if (cacheService == null)
            cacheService = new FileFormatCacheService(formatKey);
        List<LineAttributes> list = (names != null && names.length > 0) ?
                cacheService.getLineAttrListBy(names) : cacheService.getAllLineAttrList();

        DefaultInputFormat inputFormat = new DefaultInputFormat();
        inputFormat.setLineAttrList(list);

        return inputFormat;
    }


}
