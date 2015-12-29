package org.g6.bigdata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.g6.bigdata.exception.BigDataRuntimeException;

import java.io.IOException;

public class HDFSUtil {
    public void put2HDFS(String src, String dst, Configuration conf) {
        Path dstPath = new Path(dst);
        try {
            FileSystem hdfs = dstPath.getFileSystem(conf);
            hdfs.copyFromLocalFile(false, new Path(src), dstPath);
        } catch (IOException ie) {
            StringBuffer sb = new StringBuffer();
            sb.append("Exception happen while putting ")
                    .append(src)
                    .append(" in ")
                    .append(dst);
            throw new BigDataRuntimeException(sb.toString(), ie);
        }
    }

}
