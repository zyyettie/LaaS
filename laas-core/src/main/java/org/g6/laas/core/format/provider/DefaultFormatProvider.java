package org.g6.laas.core.format.provider;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.format.DefaultInputFormat;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.cache.InputFormatCache;
import org.g6.laas.core.log.line.LineAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Data
@Component
public final class DefaultFormatProvider extends FileFormatProvider {
    private String key;
    @Autowired
    private InputFormatCache cache;

    public DefaultFormatProvider(String key) {
        this.key = key;
    }

    @Override
    protected InputFormat parse() {
        //TODO this check will be removed once Spring injection is available
        if(cache == null)
            cache = new InputFormatCache();
        Map<String, LineAttributes> lineAttrMap =cache.getAllInputFormats().get(key);
        return new DefaultInputFormat(lineAttrMap);
    }
}
