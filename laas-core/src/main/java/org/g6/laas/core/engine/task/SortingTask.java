package org.g6.laas.core.engine.task;

import org.g6.laas.core.log.Line;
import org.g6.laas.core.rule.Rule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public abstract class SortingTask extends AbstractAnalysisTask<Collection<Line>> {
    protected void processRules() {
        Iterator<? extends Line> lines = openReader();
        Collection<Rule> rules = getContext().getRules();
        Collection<Line> lineList = new ArrayList<>();

        while (lines.hasNext()) {
            Line line = lines.next();
            for (Rule rule : rules) {
                if (rule.isSatisfied(line.getContent())) {
                    line.split();
                    lineList.add(line);
                    break;
                }
            }
        }

        getContext().set("SORTING", lineList);
    }
}
