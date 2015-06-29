package org.g6.laas.core.engine;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.exception.LaaSRuntimeException;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
public class StrategyAnalysisEngine implements AnalysisEngine {

  private ExecutionStrategy strategy;
  private boolean isShutdown = false;

  public void shutdown() {
     isShutdown = true;
  }

  public <T> Future<T> submit(final AnalysisTask<T> task) {
    if(isShutdown)
      throw new LaaSRuntimeException("analysis engine has been shutdown can not submit more tasks.");
    Callable<T> callable = new Callable<T>() {
      public T call() throws Exception {
        return task.analyze();
      }
    };
    return strategy.execute(callable);
  }
}
