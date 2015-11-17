package org.g6.laas.sm.validator;

import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.validator.FileValidator;
import org.g6.util.FileUtil;
import org.g6.util.RegexUtil;

import java.io.File;
import java.util.List;

public class RTELogValidator implements FileValidator {
    @Override
    public boolean validate(ILogFile file) {
        boolean isFile = FileUtil.isFile(file.getPath());

        List<String> firstLines = FileUtil.readFirstNLines(new File(file.getPath()), 10L);
        List<String> lastLines = FileUtil.readLastNLines(new File(file.getPath()), 10L);

        return isFile && (isValidFile(firstLines) || isValidFile(lastLines));
    }

    private boolean isValidFile(List<String> lines){
        for(String line : lines){
            if (RegexUtil.isMatched(line, "^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE ")) {
                return true;
            }
        }
        return false;
    }

}

