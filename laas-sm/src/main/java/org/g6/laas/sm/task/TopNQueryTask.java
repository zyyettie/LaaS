package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.engine.task.SortingTask;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LineComparator;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TopNQueryTask extends SortingTask {
    private int N = 50;

    @Override
    protected Collection<Line> process() {
        List<Line> lines = (List<Line>) getContext().get("SORTING");
        Collections.sort(lines, new LineComparator());

        List<Line> topNList = new ArrayList<>();

        int counter = 0;
        for (Line line : lines) {
            if (counter < N) {
                topNList.add(line);
            }
            counter++;
        }
        return lines;
    }

    public TopNQueryTask(AnalysisContext context) {
        this.N = context.get("COUNTER") != null ? ((Integer) context.get("COUNTER")).intValue() : N;
        setContext(context);
    }
}
