package org.g6.laas.core.rule;


import org.g6.laas.core.rule.action.ActionCondition;
import org.g6.laas.core.rule.action.RuleAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractRule implements Rule {

  private Map<ActionCondition, List<RuleAction>> actions = new HashMap<>();

  public Rule and(Rule rule) {
    return new AndRule(this, rule);
  }

  public Rule or(Rule rule) {
    return new OrRule(this, rule);
  }

  public Rule not() {
    return new NotRule(this);
  }

  public void triggerAction(ActionCondition condition, Object content) {
    List<RuleAction> actionList = actions.get(condition);
    if (actionList != null) {
      for (RuleAction action : actionList) {
        action.doAction(content);
      }
    }
  }

  @Override
  public void addActionListener(RuleAction action, ActionCondition condition) {
    List<RuleAction> actionList = actions.get(condition);
    if (actionList == null) {
      actionList = new ArrayList<>();
      actionList.add(action);
      actions.put(condition, actionList);
    } else {
      actionList.add(action);
    }
  }

  class NotRule extends AbstractRule {
    private Rule wrapper;

    public NotRule(Rule rule) {
      this.wrapper = rule;
    }

    public boolean isSatisfied(Object content) {
      return !wrapper.isSatisfied(content);
    }

    @Override
    public String toString(){
      return "not " + wrapper.toString();
    }
  }

  class AndRule extends AbstractRule {
    private Rule one;
    private Rule other;

    public AndRule(Rule one, Rule other) {
      this.one = one;
      this.other = other;
    }

    public boolean isSatisfied(Object content) {
      return one.isSatisfied(content) && other.isSatisfied(content);
    }

    @Override
    public String toString(){
      return one.toString() + " and " + other.toString();
    }
  }

  class OrRule extends AbstractRule {
    private Rule one;
    private Rule other;

    public OrRule(Rule one, Rule other) {
      this.one = one;
      this.other = other;
    }

    public boolean isSatisfied(Object content) {
      return one.isSatisfied(content) || other.isSatisfied(content);
    }

    @Override
    public String toString(){
      return one.toString() + " or " + other.toString();
    }
  }
}
