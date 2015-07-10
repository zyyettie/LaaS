package org.g6.laas.core.file;

public interface ILogFile {
    String getName();
    String getPath();
    String getFormatKey();
    boolean isValid();
}
