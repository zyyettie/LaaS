package org.g6.laas.core.cache;

import org.g6.laas.core.format.cache.FileFormatCache;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InputFormatCacheBean implements InitializingBean {
    @Autowired
    private FileFormatCache cache;


    public void afterPropertiesSet() throws Exception {
        //cache.getAllInputFormats();
    }
}
