package org.g6.laas.core.rule.action;


import lombok.Data;
import org.g6.laas.core.engine.context.AnalysisContext;

@Data
public abstract class AbstractRuleAction implements RuleAction {

    private AnalysisContext context;

    public AbstractRuleAction(AnalysisContext context) {
        this.context = context;
    }

}
