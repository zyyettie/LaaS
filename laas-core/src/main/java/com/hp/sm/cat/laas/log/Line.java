package com.hp.sm.cat.laas.log;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@NoArgsConstructor
public abstract class Line extends Slice {

  private int lineNumber;

  public Line(String content, File file, int lineNumber) {
    super(file, content, lineNumber, lineNumber + 1);
    this.lineNumber = lineNumber;
  }
}
