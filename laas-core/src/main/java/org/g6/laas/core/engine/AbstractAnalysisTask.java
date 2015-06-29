package org.g6.laas.core.engine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LogFileReader;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.Rule;

import java.io.IOException;
import java.util.Iterator;

@Slf4j
@Data
public abstract class AbstractAnalysisTask<T> implements AnalysisTask<T>{
  protected void started(){
    log.info("Task " + this.toString() + " started");
  };

  protected abstract T process() throws LaaSRuntimeException;

  protected void finished(){
    log.info("Task " + this.toString() + " finished");
  }

  private AnalysisContext context;

  public T analyze() {
    started();
    T result = process();
    finished();
    return result;
  }
}
