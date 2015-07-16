package org.g6.laas.core.cache;

import org.g6.laas.core.format.cache.InputFormatCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputFormatCacheBean implements InitializingBean {
    @Autowired
    private InputFormatCache cache;

    @Override
    public void afterPropertiesSet() throws Exception {
        //cache.getAllInputFormats();
    }
}
