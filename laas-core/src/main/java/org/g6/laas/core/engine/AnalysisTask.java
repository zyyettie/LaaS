package org.g6.laas.core.engine;

import org.g6.laas.core.engine.context.AnalysisContext;

public interface AnalysisTask<T> {
  T analyze();

  void setContext(AnalysisContext context);
}
