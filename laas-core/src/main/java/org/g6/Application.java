package org.g6;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.AnalysisTask;
import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.engine.task.SearchKeyWordsTask;
import org.g6.laas.core.exception.LaaSRuntimeException;
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

    rules.add(new KeywordRule("DBQUERY"));

    AnalysisTask<Map<String, Collection<Line>>> task = new SearchKeyWordsTask(rules);

    StrategyAnalysisEngine engine = new StrategyAnalysisEngine();

    engine.setStrategy(new ThreadPoolExecutionStrategy());

    Future<Map<String, Collection<Line>>> future = engine.submit(task);
    try {
      Map<String, Collection<Line>> result = future.get();
      log.info(result.toString());
    } catch (Exception e) {
      String errorMsg = "execute task " + task.toString() + " failed";
      log.error(errorMsg);
      throw new LaaSRuntimeException(errorMsg, e);
    }
  }
}
