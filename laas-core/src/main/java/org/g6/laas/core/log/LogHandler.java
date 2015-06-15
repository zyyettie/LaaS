package org.g6.laas.core.log;

import org.g6.laas.core.file.ILogFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LogHandler{
    private ILogFile file;
    List<ILogFile> list;
    FileReader reader;

    public LogHandler(ILogFile file, FileReader reader) {
        list = new ArrayList<ILogFile>();
        list.add(file);
        this.reader = reader;
    }

    public LogHandler(List<ILogFile> _list, FileReader reader){
        list = _list;
        this.reader = reader;
    }

    public abstract Iterator<? extends Slice> handle() throws IOException;

}
