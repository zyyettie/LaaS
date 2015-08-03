package org.g6.laas.core.engine.task;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.context.AnalysisContext;

import java.util.Map;

@Slf4j
@Data
public abstract class ChainTask implements AnalysisTask<Map<String, Object>> {
    private AnalysisContext context;
    private Map<String, Object> retMap;
    private String previous = "start";
    private String next = "end";

    protected void started() {
        log.info("Task " + this.toString() + " started");
    }

    public ChainTask(AnalysisContext context, Map<String, Object> retMap) {
        this.context = context;
        this.retMap = retMap;
    }

    public ChainTask(AnalysisContext context) {
        this.context = context;
    }

    protected void processRules() {
    }

    protected void finished() {
        log.info("Task " + this.toString() + " finished");
    }

    @Override
    public abstract Map<String, Object> analyze();
}
