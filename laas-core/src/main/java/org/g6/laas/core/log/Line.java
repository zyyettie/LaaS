package org.g6.laas.core.log;

import lombok.NoArgsConstructor;
import org.g6.laas.core.file.ILogFile;

@NoArgsConstructor
public abstract class Line extends Slice {

  public Line(ILogFile file, String content, int lineNumber) {
    super(file, content, lineNumber, lineNumber + 1);
  }

  public int getLineNumber(){
    return getStart();
  }
}
