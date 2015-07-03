package org.g6.laas.sm.format;

import org.g6.laas.core.format.LogInputFormat;

public class SMRTELogInputFormat extends LogInputFormat {
    @Override
    public boolean isDiff() {
        return true;
    }
}
