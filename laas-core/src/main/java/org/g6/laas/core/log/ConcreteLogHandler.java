package org.g6.laas.core.log;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.rule.Rule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ConcreteLogHandler extends LogHandler {

  public ConcreteLogHandler(ILogFile iLogFile, Rule rule) {
    super(iLogFile, rule);
  }

  public ConcreteLogHandler(List<ILogFile> list, Rule rule) {
    super(list, rule);
  }

  @Override
  public Iterator<? extends Line> handle(AnalysisContext context) throws IOException {
    Collection<Line> collection = new ArrayList<>();
    for (ILogFile iLogFile : list) {
      //TODO
      //Here we may need to check which file is the first one.
      //There are two ways
      //1. open each file and read the first line and check the timestamp and then compare. after that, start reading one by one
      //2. open each file and read each line and apply rules. at last order the result according to the timestamp column
      //The disadvantage of #2 is in some scenarios all files should be opened in order. for example, get RAD calling related data.
      LogFileReader reader = new LogFileReader(iLogFile);
      reader.open();
      String str;
      int number = 0;
      while ((str = reader.readLine()) != null) {
        number++;
        if (rule.isSatisfied(str)) {
          Line line = new LogLine(iLogFile, str, number, context.getInputFormat());
          collection.add(line);
        }
      }
      reader.close();
    }
    return collection.iterator();
  }
}
