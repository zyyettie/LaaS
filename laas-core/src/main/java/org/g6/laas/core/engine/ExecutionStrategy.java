package org.g6.laas.core.engine;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ExecutionStrategy {
  <T> Future<T> execute(Callable<T> c);
}
