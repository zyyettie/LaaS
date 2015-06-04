package com.hp.sm.cat.laas.log;

import lombok.Data;
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
