package org.g6.laas.core.rule.action;

import org.g6.laas.core.rule.Rule;

public interface RuleAction {

  void satisfied(Rule rule,Object content);

}
