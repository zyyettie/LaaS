package org.g6.laas.core.log;

import org.g6.laas.core.file.ILogFile;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;

public class LogFileReader implements Closeable {
    private BufferedReader reader = null;
    ILogFile file;

    LogFileReader(ILogFile file) {
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }

    public void open() throws IOException{
        reader = new BufferedReader(new java.io.FileReader(file.getPath()));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }
}
