package org.g6.laas.core.log;

import org.g6.laas.core.file.ILogFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LogHandler{
    List<ILogFile> list;

    public LogHandler(ILogFile iLogFile) {
        list = new ArrayList<ILogFile>();
        list.add(iLogFile);
    }

    public LogHandler(List<ILogFile> list){
        this.list = list;
    }

    public abstract Iterator<? extends Slice> handle() throws IOException;

}
