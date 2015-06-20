package org.g6.laas.core.log;

import org.g6.laas.LaaSContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.rule.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class LogHandler{
    List<ILogFile> list;
    Rule  rule;
    InputFormat format;

    public LogHandler(ILogFile file, Rule rule, InputFormat format) {
        list = new ArrayList<>();
        list.add(file);
        this.rule = rule;
        this.format = format;
    }

    public LogHandler(List<ILogFile> list, Rule rule, InputFormat format){
        this.list = list;
        this.rule = rule;
        this.format = format;
    }

    public void addFile(ILogFile file) {
        list.add(file);
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public abstract Iterator<? extends Slice> handle(LaaSContext context) throws IOException;

}
