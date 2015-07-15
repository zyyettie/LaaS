package org.g6.laas.core.file;

import lombok.Data;
import org.g6.laas.core.file.validator.FileValidator;
import org.g6.util.Constants;

@Data
public class HDFSFile implements ILogFile {
    public static final String SEPARATOR = "/";
    private String url;
    private FileValidator validator;

    public HDFSFile(String url) {
        this(url, null);
    }

    public HDFSFile(String url, FileValidator validator) {
        this.url = url;
        this.validator = validator;
    }

    @Override
    public String getName() {
        int slash = url.lastIndexOf(SEPARATOR);
        return url.substring(slash + 1);
    }

    @Override
    public String getPath() {
        return url;
    }

    @Override
    public boolean isValid() {
        return validator == null ? true : validator.validate(this);
    }

}
