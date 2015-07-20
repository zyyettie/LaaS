package org;

import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.line.Slice;
import org.g6.laas.sm.task.LoginTimeInfoTask;
import org.g6.laas.sm.task.RadShowTask;
import org.g6.laas.sm.task.SplitProcessAndThreadTask;
import org.g6.laas.sm.task.TopNQueryTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DemoTest {

    public static void main(String[] args) {
        //runSplitProcessAndThreadTask();
        //runTopNQueryTask();
        //runLoginTimeInfoTask();
        runRadShowTask();
    }

    static void runSplitProcessAndThreadTask() {
        SplitProcessAndThreadTask task = new SplitProcessAndThreadTask("e:\\sm.log");
        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();
        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<String> future = engine.submit(task);
        engine.shutdown();

        try {
            String zipFile = future.get();
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static void runLoginTimeInfoTask() {
        LoginTimeInfoTask task = new LoginTimeInfoTask("e:\\sm.log");
        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();
        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<Map<String, Double>> future = engine.submit(task);
        engine.shutdown();

        try {
            Map<String, Double> loginMap = future.get();
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static void runTopNQueryTask() {
        TopNQueryTask task = new TopNQueryTask(50, "e:\\sm.log");

        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();
        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<List<Line>> future = engine.submit(task);
        engine.shutdown();

        try {
            List<Line> lines = future.get();
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static void runRadShowTask() {
        RadShowTask task = new RadShowTask("C:\\sm-rtm3.log");

        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();
        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<Slice> future = engine.submit(task);
        engine.shutdown();

        try {
            Slice slice = future.get();
            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
