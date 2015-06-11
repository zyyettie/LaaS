package com.hp.sm.cat.laas.rule;


public interface  Rule {
  boolean isSatisfied(Object content);
  Rule and(Rule rule);
  Rule or(Rule rule);
  Rule not();
}
