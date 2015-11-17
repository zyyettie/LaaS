package org.g6.laas.core.file;

import lombok.Data;
import org.g6.laas.core.file.validator.FileValidator;

import java.io.File;

@Data
public class LogFile implements ILogFile {
    private String file;
    private String name;
    private String originalName;
    private FileValidator validator;

    public LogFile(String file, FileValidator validator) {
        this.file = file;
        this.validator = validator;
        this.name = getName();
    }

    public LogFile(String file, String originalName){
        this.file = file;
        this.originalName = originalName;
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
