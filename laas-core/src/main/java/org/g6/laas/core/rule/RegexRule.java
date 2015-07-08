package org.g6.laas.core.rule;

import lombok.Data;
import org.g6.util.RegexUtil;

@Data
public class RegexRule extends AbstractRule {
    private String regex;

    public RegexRule(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean isSatisfied(Object content) {
        return RegexUtil.isMatched(content.toString(), regex);
    }
}
