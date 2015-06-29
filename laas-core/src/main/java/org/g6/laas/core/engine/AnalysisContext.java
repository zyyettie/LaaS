package org.g6.laas.core.engine;


import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.Rule;

import java.util.Collection;

public interface AnalysisContext {

  InputFormat getInputFormat();

  LogHandler getHandler();

  Collection<Rule> getRules();

}
