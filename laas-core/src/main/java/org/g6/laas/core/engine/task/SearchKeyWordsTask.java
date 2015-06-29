package org.g6.laas.core.engine.task;

import org.g6.laas.core.engine.AbstractAnalysisTask;
import org.g6.laas.core.engine.AnalysisContext;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.core.log.Line;

import java.util.Collection;
import java.util.Map;

public class SearchKeyWordsTask extends AbstractAnalysisTask<Map<String,Collection<Line>>> {

  @Override
  protected Map<String, Collection<Line>> process() throws LaaSRuntimeException {
    AnalysisContext context = this.getContext();
    //todo
    return null;
  }
}
