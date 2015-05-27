package com.hp.sm.cat.laas.rule;

public class AndRule extends AbstractRule {
  private Rule one;
  private Rule other;

  public AndRule(Rule one, Rule other){
    this.one = one;
    this.other = other;
  }

  public boolean isSatisfied(Object content) {
    return one.isSatisfied(content) && other.isSatisfied(content);
  }
}
