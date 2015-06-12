package org.g6.laas.core.rule;


public interface  Rule {
  boolean isSatisfied(Object content);
  Rule and(Rule rule);
  Rule or(Rule rule);
  Rule not();
}
