package org.g6.laas.core.log;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public abstract class LogReader {

  private File file;

  public LogReader(File file) {
    this.file = file;
  }

  public abstract Iterator<? extends Slice> open() throws IOException;

  public abstract void close() throws IOException;

}
