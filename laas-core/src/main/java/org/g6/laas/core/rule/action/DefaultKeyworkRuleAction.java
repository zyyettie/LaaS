package org.g6.laas.core.rule.action;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;

import java.util.ArrayList;
import java.util.List;

public class DefaultKeyworkRuleAction extends ContextRuleAction {

  @Override
  public void doAction() {

    KeywordRule rule = (KeywordRule) getRule();

    KeywordRule keywordRule = (KeywordRule) rule;
    Line matched = (Line) keywordRule.getMatched();
    List<Line> matchedLines = (List<Line>) getContext().get(keywordRule);
    if (matchedLines == null) {
      matchedLines = new ArrayList<>();
      matchedLines.add(matched);
      getContext().set(rule, matchedLines);
    } else {
      matchedLines.add(matched);
    }

  }

  public DefaultKeyworkRuleAction(AnalysisContext context, Rule rule) {
    super(context, rule);
    if (!(rule instanceof KeywordRule)) {
      throw new LaaSRuntimeException("Default KeywordRuleAction only accept Keyword rule.");
    }
  }
}
