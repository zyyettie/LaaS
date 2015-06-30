package org.g6.laas.core.rule.action;


import lombok.Data;
import org.g6.laas.core.rule.Rule;

@Data
public abstract class AbstractRuleAction implements RuleAction {

  private Rule rule;

  AbstractRuleAction(Rule rule) {
    this.rule = rule;
  }
}
