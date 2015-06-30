package org.g6.laas.core.rule.action;

import lombok.Data;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.rule.Rule;

@Data
public abstract class ContextRuleAction extends AbstractRuleAction {

  private AnalysisContext context;

  public ContextRuleAction(AnalysisContext context) {
    this.context = context;
  }
}
