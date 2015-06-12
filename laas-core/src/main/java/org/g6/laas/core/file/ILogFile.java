package org.g6.laas.core.file;

import java.io.BufferedReader;
import java.io.IOException;

public interface ILogFile {
    String getName();
    BufferedReader getReader() throws Exception;
    void close() throws IOException;
}
