package org.g6.laas.core.file;

import lombok.Data;
import org.g6.laas.core.file.validator.FileValidator;
import org.g6.util.Constants;

import java.io.File;

@Data
public class LogFile implements ILogFile {
    private String file;

    private FileValidator validator;

    public LogFile(String file) {
        this(file, null);
    }

    public LogFile(String file, FileValidator validator) {
        this.file = file;
        this.validator = validator;
    }

    @Override
    public String getName() {
        return new File(file).getName();
    }

    @Override
    public String getPath() {
        return file;
    }

    @Override
    public boolean isValid() {
        return validator == null ? true : validator.validate(this);
    }
}
