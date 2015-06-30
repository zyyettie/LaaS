package org.g6;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.SearchKeyWordsTask;
import org.g6.laas.core.exception.LaaSRuntimeException;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.log.ConcreteLogHandler;
import org.g6.laas.core.log.Line;
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

    rules.add(new KeywordRule("DBQUERY").or(new KeywordRule("DBFIND")));

    SimpleAnalysisContext context = new SimpleAnalysisContext();
    context.setHandler(new ConcreteLogHandler(new LogFile("C:\\gitRepo\\LaaS\\laas-core\\src\\main\\resources\\RTE_log_format.txt"), null));
    context.setRules(rules);
    SearchKeyWordsTask task = new SearchKeyWordsTask(context);

    StrategyAnalysisEngine engine = new StrategyAnalysisEngine();

    engine.setStrategy(new ThreadPoolExecutionStrategy());

    Future<Map<Rule, Collection<Line>>> future = engine.submit(task);
    try {
      Map<Rule, Collection<Line>> result = future.get();
      log.info("Task execute result: ");
      log.info("******************************************");
      for (Map.Entry<Rule, Collection<Line>> entry : result.entrySet()) {
        log.info(entry.getKey().getClass().toString());
        for (Line line : entry.getValue()) {
          log.info(line.toString());
        }
        log.info("******************************************");
      }
    } catch (Exception e) {
      String errorMsg = "execute task " + task.toString() + " failed";
      log.error(errorMsg);
      throw new LaaSRuntimeException(errorMsg, e);
    }
  }
}
