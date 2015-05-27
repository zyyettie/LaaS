package com.hp.sm.cat.laas.rule;


public class NotRule extends AbstractRule{
  private Rule wrapper;

  public NotRule(Rule rule){
    this.wrapper = rule;
  }

  public boolean isSatisfied(Object content) {
    return !wrapper.isSatisfied(content);
  }
}
