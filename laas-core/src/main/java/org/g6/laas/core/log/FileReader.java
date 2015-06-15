package org.g6.laas.core.log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;

public interface FileReader extends Closeable {
    abstract BufferedReader createReader() throws Exception;
    public abstract void close() throws IOException;
}
