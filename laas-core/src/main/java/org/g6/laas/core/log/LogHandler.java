package org.g6.laas.core.log;

import org.g6.laas.core.file.ILogFile;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LogHandler implements Closeable {
    private ILogFile file;
    List<ILogFile> list;

    public LogHandler(ILogFile file) {
        list = new ArrayList<ILogFile>();
        list.add(file);
    }

    public LogHandler(List<ILogFile> _list){
        list = _list;
    }

    public abstract Iterator<? extends Slice> handle() throws IOException;

    public void close() throws IOException {
        file.close();
    }

}
