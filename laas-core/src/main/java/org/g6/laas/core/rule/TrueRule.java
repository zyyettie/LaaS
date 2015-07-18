package org.g6.laas.core.rule;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TrueRule extends AbstractRule {
    @Override
    public boolean isSatisfied(Object content) {
        return true;
    }
}
