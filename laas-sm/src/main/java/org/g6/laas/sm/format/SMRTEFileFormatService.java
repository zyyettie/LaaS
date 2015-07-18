package org.g6.laas.sm.format;

import org.g6.laas.core.format.FileFormatCacheService;
import org.springframework.stereotype.Service;

@Service
public class SMRTEFileFormatService extends FileFormatCacheService{

    public SMRTEFileFormatService() {
        super("SMRTE_SM_LOG");
    }
}
