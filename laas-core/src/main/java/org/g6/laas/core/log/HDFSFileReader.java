package org.g6.laas.core.log;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.g6.laas.core.file.ILogFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HDFSFileReader implements FileReader {
    BufferedReader reader;
    ILogFile file;

    HDFSFileReader(ILogFile file) {
        this.file = file;
    }

    @Override
    public BufferedReader createReader() throws Exception {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        Path p = new Path(file.getPath());
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
