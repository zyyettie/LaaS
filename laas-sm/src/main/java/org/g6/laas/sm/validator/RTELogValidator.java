package org.g6.laas.sm.validator;

import org.g6.laas.core.exception.LaaSExceptionHandler;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.validator.FileValidator;
import org.g6.laas.core.log.reader.LogFileReader;
import org.g6.laas.sm.exception.SMRuntimeException;
import org.g6.util.FileUtil;
import org.g6.util.RegexUtil;

import java.io.IOException;

public class RTELogValidator implements FileValidator {
    @Override
    public boolean validate(ILogFile file) {
        boolean isFile = FileUtil.isFile(file.getPath());

        boolean isRTE = false;
        try (LogFileReader reader = new LogFileReader(file)) {
            reader.open();
            String content = reader.readLine();
            if (RegexUtil.isMatched(content, "RTE D")) {
                isRTE = true;
            }
        } catch (IOException e) {
            LaaSExceptionHandler.handleException("Exception happened while validating file: "
                    + file.getPath(), new SMRuntimeException(e));
        }

        return isFile && isRTE;
    }
}
