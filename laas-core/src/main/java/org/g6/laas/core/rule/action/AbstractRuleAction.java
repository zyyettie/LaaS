package org.g6.laas.core.rule.action;


import lombok.Data;
import org.g6.laas.core.rule.Rule;

@Data
public abstract class AbstractRuleAction implements RuleAction {

  private Rule rule;

  @Override
  public void bindRule(Rule rule){
    this.setRule(rule);
  }
}
