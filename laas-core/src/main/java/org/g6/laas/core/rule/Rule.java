package org.g6.laas.core.rule;


import org.g6.laas.core.rule.action.RuleAction;

public interface Rule {
    boolean isSatisfied(Object content);

    Rule and(Rule rule);

    Rule or(Rule rule);

    Rule not();

    void addActionListener(RuleAction action);

    void triggerAction(Object content);
}
