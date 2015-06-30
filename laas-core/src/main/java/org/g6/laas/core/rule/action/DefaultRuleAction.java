package org.g6.laas.core.rule.action;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;

import java.util.ArrayList;
import java.util.List;

public class DefaultRuleAction extends ContextRuleAction {

  @Override
  public void doAction(Object content) {

    List<Line> matchedLines = (List<Line>) getContext().get(getRule());
    if (matchedLines == null) {
      matchedLines = new ArrayList<>();
      matchedLines.add((Line)content);
      getContext().set(getRule(), matchedLines);
    } else {
      matchedLines.add((Line)content);
    }
  }

  public DefaultRuleAction(AnalysisContext context) {
    super(context);
  }
}
