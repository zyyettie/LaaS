package org.g6.laas.core.engine.task.workflow;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.task.ChainTask;
import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.rule.Rule;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public abstract class ChainFileTask extends ChainTask {

    public ChainFileTask(AnalysisContext context, Map<String, Object> retMap) {
        super(context, retMap);
    }

    public ChainFileTask(AnalysisContext context) {
        super(context);
    }

    protected abstract Map<String, Object> process();

    protected Iterator<? extends Line> openReader() {
        try {
            return getContext().getHandler().handle(getContext());
        } catch (IOException e) {
            throw new LaaSCoreRuntimeException("open log handler failed.");
        }
    }

    protected void processRules() {
        Iterator<? extends Line> lines = openReader();
        Collection<Rule> rules = getContext().getRules();
        while (lines.hasNext()) {
            Line line = lines.next();
            for (Rule rule : rules) {
                if (rule.isSatisfied(line.getContent())) {
                    rule.triggerAction(line);
                }
            }
        }
    }

    public Map<String, Object> analyze() {
        started();
        processRules();
        Map<String, Object> result = process();
        finished();
        return result;
    }

}
