package org.g6.laas.core.file;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HDFSFile implements ILogFile {
    private String fileName;
    public static final String SEPARATOR = "/";
    BufferedReader reader;

    public HDFSFile(String _fileName) {
        fileName = _fileName;
    }

    @Override
    public String getName() {
        int slash = fileName.lastIndexOf(SEPARATOR);
        return fileName.substring(slash+1);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        Path p = new Path(fileName);
        FSDataInputStream in = fs.open(p);
        reader = new BufferedReader(new InputStreamReader(in));

        return reader;
    }

    @Override
    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }
}
