package org.g6.laas.core.log.handler;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.filter.IFilter;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.rule.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
public abstract class LogHandler {
    private List<ILogFile> fileList;
    private Rule rule;
    private IFilter filter;


    public LogHandler(ILogFile file, IFilter filter) {

        fileList = new ArrayList<>();
        fileList.add(file);
        this.filter = filter;
    }

    public LogHandler(List<ILogFile> list, IFilter filter) {
        this.fileList = list;
        this.filter = filter;
    }

    public void addFile(ILogFile file) {
        fileList.add(file);
    }

    public void setFilter(IFilter filter) {
        this.filter = filter;
    }

    protected void preHandle() {
    }

    public abstract Iterator<? extends Line> handle(AnalysisContext context) throws IOException;

}
