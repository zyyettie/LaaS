package org.g6.laas.core.file;

import org.g6.util.Constants;

public class HDFSFile implements ILogFile {
    private String url;
    public static final String SEPARATOR = "/";

    public HDFSFile(String url) {
        this.url = url;
    }

    @Override
    public String getName() {
        int slash = url.lastIndexOf(SEPARATOR);
        return url.substring(slash+1);
    }

    @Override
    public String getPath() {
        return url;
    }

    @Override
    public int getType() {
        return Constants.LOG_TYPE_HDFS_FILE;
    }

}
