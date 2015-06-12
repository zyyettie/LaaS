package org.g6.laas.core.file;

import java.io.*;

public class LogFile implements ILogFile {
    private File file;
    BufferedReader reader = null;

    public LogFile(String fileName) {
        file = new File(fileName);
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public BufferedReader getReader() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(file));
        return reader;
    }

    @Override
    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }


}
