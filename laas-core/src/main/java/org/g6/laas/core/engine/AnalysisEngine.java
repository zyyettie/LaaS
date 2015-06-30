package org.g6.laas.core.engine;

import org.g6.laas.core.engine.task.AnalysisTask;

import java.util.concurrent.Future;

public interface AnalysisEngine {

  <T> Future<T> submit(AnalysisTask<T> task);

  void shutdown();

}
