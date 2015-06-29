package org.g6.laas.core.engine;

import java.util.concurrent.Future;

public interface AnalysisEngine {

  <T> Future<T> submit(AnalysisTask<T> task);

  void shutdown();

}
