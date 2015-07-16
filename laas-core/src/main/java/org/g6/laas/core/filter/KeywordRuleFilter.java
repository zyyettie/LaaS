package org.g6.laas.core.filter;

import org.g6.laas.core.rule.KeywordRule;

public class KeywordRuleFilter extends AbstractRuleFilter {
    public KeywordRuleFilter(String keyword) {
        this.setRule(new KeywordRule(keyword));
    }
}
