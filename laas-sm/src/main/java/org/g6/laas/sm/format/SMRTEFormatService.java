package org.g6.laas.sm.format;

import org.g6.laas.core.format.FileFormatCacheService;

/**
 * Format service for SM RTE
 *
 * @author Johnson Jiang
 * @version 1.0
 */
public class SMRTEFormatService extends FileFormatCacheService {
    public SMRTEFormatService() {
        setFormatKey("SMRTE_SM_LOG");
    }
}
