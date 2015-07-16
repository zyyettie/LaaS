package org.g6.laas.core.filter;

import org.g6.laas.core.rule.RegexRule;

public class RegexRuleFilter extends AbstractRuleFilter {
    public RegexRuleFilter(String regex) {
        this.setRule(new RegexRule(regex));
    }
}
