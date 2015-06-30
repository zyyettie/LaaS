package org.g6.laas.core.engine;

import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
public class ThreadPoolExecutionStrategy implements ExecutionStrategy {

  private ExecutorService executor = Executors.newCachedThreadPool();

  public <T> Future<T> execute(Callable<T> c) {
    return executor.submit(c);
  }
}
