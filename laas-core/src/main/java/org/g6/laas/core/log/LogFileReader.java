package org.g6.laas.core.log;

import org.g6.laas.core.file.ILogFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LogFileReader{
    private BufferedReader reader = null;
    ILogFile file;

    LogFileReader(ILogFile file) {
        this.file = file;
    }

    public void open() throws IOException{
        reader = new BufferedReader(new FileReader(file.getPath()));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }

}
