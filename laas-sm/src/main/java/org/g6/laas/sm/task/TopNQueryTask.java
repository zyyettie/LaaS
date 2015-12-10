package org.g6.laas.sm.task;

import com.google.common.collect.Ordering;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.line.LineComparator;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Get top N DB query from SM RTE log file
 *
 * @author Johnson Jiang
 * @version 1.0
 * @since 1.0
 */
public class TopNQueryTask extends SMRTETask<List<Line>> {
    private int N = 50;
    private String order = "desc";
    private String category;
    private List<Line> lines = new ArrayList<>();

    private final String CATEGORY_DBQUERY = "DBQUERY";
    private final String CATEGORY_SCRIPTTRACE = "SCRIPTTRACE";

    @Override
    protected List<Line> process() {
        Ordering ordering = Ordering.from(new LineComparator());
        return order.equalsIgnoreCase("desc") ? ordering.leastOf(lines, N) : ordering.greatestOf(lines, N);
    }

    @Override
    void initRule() {
        Rule rule = null;
        if (category.equals(CATEGORY_DBQUERY)) {
            rule = new KeywordRule("RTE D DBQUERY")
                    .or(new RegexRule("^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D DBFIND(?:\\^[^\\^]+){6}\\^(\\d+\\.\\d+)"))
                    .or(new RegexRule("^\\s*(\\d+)\\(\\s+(\\d+)\\)\\s+(\\d+/\\d+/\\d+\\s+\\d+:\\d+:\\d+)\\s+RTE D.+DBACCESS.+(\\d+\\.\\d+)\\s+seconds"));

        } else if (category.equals(CATEGORY_SCRIPTTRACE)) {
            rule = new RegexRule("^[\\s:/\\w\\(\\)]+RTE D SCRIPTTRACE:\\s(.+)\\sexited.+elapsed: (\\d+) ms");
        }

        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                line.split();
                lines.add(line);
            }
        });
        addRule(rule);
    }

    DefaultInputFormatProvider getProvider() {
        String[] formats = null;
        if (category.equals(CATEGORY_DBQUERY)) {
            formats = new String[]{"DBQUERY", "DBFIND", "DBACCESS"};
        } else if (category.equals(CATEGORY_SCRIPTTRACE)) {
            formats = new String[]{"SCRIPTTRACE"};
        }
        return getDefaultProvider(formats);
    }
}
