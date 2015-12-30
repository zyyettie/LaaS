package org.g6.bigdata.hdfs;

import lombok.Data;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.g6.bigdata.exception.BigDataRuntimeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

@Data
public class HDFSUtil {
    private String serverUri;
    private Configuration conf = new Configuration();

    /**
     * Upload local file to Hadoop HDFS system
     *
     * @param local  local path with file name
     * @param remote remote path without file name
     */
    public void put(String local, String remote) {
        Path dstPath = new Path(serverUri + remote);
        try (FileSystem fs = FileSystem.get(URI.create(serverUri), this.conf)) {
            fs.copyFromLocalFile(false, new Path(local), dstPath);
        } catch (IOException ie) {
            StringBuffer sb = new StringBuffer();
            sb.append("Exception happen while putting ")
                    .append(local)
                    .append(" in ")
                    .append(remote);
            throw new BigDataRuntimeException(sb.toString(), ie);
        }
    }

    /**
     * upload a file from stream to HDFS system
     *
     * @param inStream  the stream of a file
     * @param remote    remote file in HDFS system
     */
    public void put(InputStream inStream, String remote) {
        try (FileSystem fs = FileSystem.get(URI.create(serverUri), this.conf);
             OutputStream outStream = fs.create(new Path(remote), new Progressable() {
                 public void progress() {
                     System.out.print('.');
                 }
             });) {
            IOUtils.copyBytes(inStream, outStream, 4096, true);
        } catch (IOException e) {
            StringBuffer sb = new StringBuffer();
            sb.append("Exception happen while putting ")
                    .append(" file in ")
                    .append(remote);
            throw new BigDataRuntimeException(sb.toString(), e);
        }
    }

    /**
     * get file from HDFS system to local machine
     *
     * @param local  local path
     * @param remote remote path with file name
     */
    public void get(String local, String remote) {
        Path dstPath = new Path(serverUri + remote);
        try (FileSystem fs = FileSystem.get(URI.create(serverUri), this.conf)) {
            fs.copyToLocalFile(false, new Path(local), dstPath);
        } catch (IOException ie) {
            StringBuffer sb = new StringBuffer();
            sb.append("Exception happen while getting ")
                    .append(remote)
                    .append(" to ")
                    .append(local);
            throw new BigDataRuntimeException(sb.toString(), ie);
        }
    }

    /**
     * Del a file in HDFS system
     *
     * @param remote remote path including file name
     */
    public void del(final String remote) {
        Path dstPath = new Path(remote);
        try (FileSystem fs = FileSystem.get(URI.create(serverUri), this.conf)){
            if (fs.exists(dstPath)) {
                fs.delete(dstPath, true);
            } else {
                throw new BigDataRuntimeException(new FileNotFoundException("Not found" + remote));
            }
        } catch (IOException ie) {
            StringBuffer sb = new StringBuffer();
            sb.append("Exception happen while deleting ")
                    .append(remote);
            throw new BigDataRuntimeException(sb.toString(), ie);
        }
    }


}
