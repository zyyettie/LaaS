package org.g6.laas.core.log;

import lombok.NoArgsConstructor;

import java.io.File;

@NoArgsConstructor
public abstract class Line extends Slice {

  public Line(File file, String content, int lineNumber) {
    super(file, content, lineNumber, lineNumber + 1);
  }

  public int getLineNumber(){
    return getStart();
  }
}
