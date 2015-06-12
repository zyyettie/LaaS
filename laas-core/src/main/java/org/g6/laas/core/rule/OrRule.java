package org.g6.laas.core.rule;

public class OrRule extends AbstractRule{

  private Rule one;
  private Rule other;

  public OrRule(Rule one, Rule other){
    this.one = one;
    this.other = other;
  }

  public boolean isSatisfied(Object content) {
    return one.isSatisfied(content) || other.isSatisfied(content);
  }
}
