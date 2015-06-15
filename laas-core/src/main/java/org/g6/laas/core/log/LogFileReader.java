package org.g6.laas.core.log;

import org.g6.laas.core.file.ILogFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class LogFileReader implements FileReader {
    BufferedReader reader = null;
    ILogFile file;

    LogFileReader(ILogFile file) {
        this.file = file;
    }

    @Override
    public BufferedReader createReader() throws Exception {
        File f = new File(file.getPath());
        reader = new BufferedReader(new java.io.FileReader(f));
        return reader;
    }

    @Override
    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }
}
