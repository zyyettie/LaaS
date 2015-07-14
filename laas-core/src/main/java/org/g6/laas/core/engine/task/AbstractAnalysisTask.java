package org.g6.laas.core.engine.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.rule.Rule;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Slf4j
@Data
@NoArgsConstructor
public abstract class AbstractAnalysisTask<T> implements AnalysisTask<T> {

    private AnalysisContext context = new SimpleAnalysisContext();

    public AbstractAnalysisTask(AnalysisContext context) {
        this.context = context;
    }

    protected void started() {
        log.info("Task " + this.toString() + " started");
    }

    protected abstract T process();

    protected Iterator<? extends Line> openReader() {
        try {
            return context.getHandler().handle();
        } catch (IOException e) {
            throw new LaaSCoreRuntimeException("open log handler failed.");
        }
    }

    protected void processRules() {
        Iterator<? extends Line> lines = openReader();
        Collection<Rule> rules = context.getRules();
        while (lines.hasNext()) {
            Line line = lines.next();
            for (Rule rule : rules) {
                if (rule.isSatisfied(line.getContent())) {
                    rule.triggerAction(line);
                }
            }
        }
    }

    protected void finished() {
        log.info("Task " + this.toString() + " finished");
    }

    public T analyze() {
        started();
        processRules();
        T result = process();
        finished();
        return result;
    }
}
