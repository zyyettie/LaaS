package com.hp.sm.cat.laas.rule;


public abstract class AbstractRule implements Rule{

  public Rule and(Rule rule) {
    return new AndRule(this,rule);
  }

  public Rule or(Rule rule) {
    return new OrRule(this,rule);
  }

  public Rule not() {
    return new NotRule(this);
  }
}
