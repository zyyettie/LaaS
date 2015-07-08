package org.g6.laas.core.log;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.validator.FileValidator;
import org.g6.laas.core.filter.IFilter;
import org.g6.laas.core.rule.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
@NoArgsConstructor
public abstract class LogHandler {
    private List<ILogFile> list;
    private Rule rule;
    private IFilter filter;


  public LogHandler(ILogFile file, IFilter filter) {

    list = new ArrayList<>();
    list.add(file);
    this.filter = filter;
  }

  public LogHandler(List<ILogFile> list, IFilter filter) {
    this(list,filter,null);
  }

  public LogHandler(List<ILogFile> list, IFilter filter, FileValidator validator) {
    this.list = list;
    this.filter = filter;
  }

  public void addFile(ILogFile file) {
    list.add(file);
  }

  public void setFilter(IFilter filter) {
    this.filter = filter;
  }

  public abstract Iterator<? extends Line> handle(AnalysisContext context) throws IOException;

}
