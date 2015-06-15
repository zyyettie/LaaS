package org.g6.laas.core.file;

import org.g6.util.Constants;

import java.io.File;

public class LogFile implements ILogFile {
    private String file;

    public LogFile(String file) {
        this.file = file;
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
    public int getType() {
        return Constants.LOG_TYPE_FILE;
    }

}
