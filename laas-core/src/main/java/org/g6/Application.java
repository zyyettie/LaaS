package org.g6;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.engine.task.SearchKeyWordsTask;
import org.g6.laas.core.exception.LaaSCoreRuntimeException;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.log.BasicLogHandler;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.Future;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Slf4j
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);

        Collection<Rule> rules = new ArrayList<>();

        Rule rule1 = new KeywordRule("DBQUERY");
        Rule rule2 = new KeywordRule("DBFIND");
        Rule rule3 = rule1.and(rule2);
        Rule rule4 = rule1.or(rule2);

        rules.add(rule1);
        rules.add(rule2);
        rules.add(rule3);
        rules.add(rule4);

        LogHandler handler = new BasicLogHandler(new LogFile("C:\\gitRepo\\LaaS\\laas-core\\src\\main\\resources\\RTE_log_format.txt"), null);


        SearchKeyWordsTask task = new SearchKeyWordsTask(rules, handler);

        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();

        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<Map<Rule, Collection<Line>>> future = engine.submit(task);
        engine.shutdown();
        try {
            Map<Rule, Collection<Line>> result = future.get();
            log.info("Task execute result: ");
            log.info("_______________________________________________");
            for (Map.Entry<Rule, Collection<Line>> entry : result.entrySet()) {
                log.info("******************************************");
                log.info(entry.getKey().toString());
                log.info("******************************************");
                for (Line line : entry.getValue()) {
                    log.info(line.toString());
                }
            }
        } catch (Exception e) {
            String errorMsg = "execute task " + task.toString() + " failed";
            log.error(errorMsg);
            throw new LaaSCoreRuntimeException(errorMsg, e);
        }
    }
}
