package org.g6.laas.sm.validator;

import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.validator.FileValidator;

public class RTELogValidator implements FileValidator {
    @Override
    public boolean validate(ILogFile file) {
        return false;
    }
}
