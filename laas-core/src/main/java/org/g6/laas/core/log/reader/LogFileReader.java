package org.g6.laas.core.log.reader;

import org.g6.laas.core.file.ILogFile;

import java.io.*;

public class LogFileReader implements Closeable {
    ILogFile file;
    private BufferedReader reader = null;

    public LogFileReader(ILogFile file) {
        this.file = file;
    }

    public void open() throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(file.getPath())));
        reader = new BufferedReader(new InputStreamReader(bis, "utf-8"), 10 * 1024 * 1024);
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }

}
