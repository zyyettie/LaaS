package org.g6.laas.sm.task;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.result.SplitResult;
import org.g6.laas.core.rule.RegexRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.core.rule.action.RuleAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This task is to calculate the SM login related time at RTE side
 *
 * @author Johnson Jiang
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class SMEnvInfoTask extends SMRTETask<Map<String, Double>> {
    private List<Line> lines;
    private SplitResult result;
    private String user;
    private String startTime;
    private String endTime;

    @Override
    protected void started() {
        lines = new ArrayList<>();

        Rule rule = new RegexRule("RTE D Response-Total.+format:sc\\.manage\\.ToDo\\.g application:display").or(
                new RegexRule("")
        );

        rule.addAction(new RuleAction() {
            @Override
            public void satisfied(Rule rule, Object content) {
                Line line = (Line) content;
                lines.add(line);
            }
        });
        addRule(rule);
        super.started();
    }

    @Override
    DefaultInputFormatProvider getProvider() {
        return null;
    }

    @Override
    protected Map<String, Double> process() {
        if (!lines.isEmpty()) {
            Map<String, Double> resultMap = new HashMap<>();
            resultMap.put("login_time", (Double) result.get("login_time").getValue());
            resultMap.put("rad_time", (Double) result.get("rad_time").getValue());
            resultMap.put("js_time", (Double) result.get("js_time").getValue());
            resultMap.put("log_time", (Double) result.get("log_time").getValue());
            resultMap.put("db_time", (Double) result.get("db_time").getValue());
            resultMap.put("cpu_time", (Double) result.get("cpu_time").getValue());
            return resultMap;
        }
        return null;
    }
}
