package org.g6.laas.core.log;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.validator.FileValidator;
import org.g6.laas.core.format.InputFormat;
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

  public LogHandler(ILogFile file, Rule rule) {

    list = new ArrayList<>();
    list.add(file);
    this.rule = rule;
  }

  public LogHandler(List<ILogFile> list, Rule rule) {
    this.list = list;
    this.rule = rule;
  }

  public void addFile(ILogFile file) {
    list.add(file);
  }

  public void setRule(Rule rule) {
    this.rule = rule;
  }

  public abstract Iterator<? extends Line> handle(AnalysisContext context) throws IOException;

}
