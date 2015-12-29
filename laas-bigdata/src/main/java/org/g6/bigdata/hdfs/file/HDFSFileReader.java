package org.g6.bigdata.hdfs.file;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.g6.laas.core.file.ILogFile;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;

public class HDFSFileReader implements Closeable {
    BufferedReader reader;
    ILogFile file;

    HDFSFileReader(ILogFile file) {
        this.file = file;
    }

    public void open() throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        Path p = new Path(file.getPath());
        FSDataInputStream in = fs.open(p);
        reader = new BufferedReader(new InputStreamReader(in));
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    @Override
    public void close() throws IOException {
        if (reader != null)
            reader.close();
    }
}
