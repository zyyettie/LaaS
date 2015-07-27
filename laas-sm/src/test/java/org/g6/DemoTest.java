package org.g6;

import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.log.line.Line;
import org.g6.laas.core.log.unit.LineSetUnit;
import org.g6.laas.sm.task.*;
import org.g6.util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DemoTest {

    public static void main(String[] args) {
        //SpringApplication.run(DemoTest.class, args);
        //runSplitProcessAndThreadTask();
        //runTopNQueryTask();
        //runLoginTimeInfoTask();
        runRadShowTask();
        //runSMOMiPerformanceTask();
    }

    static void runSplitProcessAndThreadTask() {
        SplitProcessAndThreadTask task = new SplitProcessAndThreadTask(new String[]{"e:\\SM_UCMDB.log"});
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
        String inputFile = "e:\\sm.log";
        String outputFile = "e:\\sm_target.log";
        LoginTimeInfoTask task = new LoginTimeInfoTask(inputFile);
        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();
        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<Map<String, Double>> future = engine.submit(task);
        engine.shutdown();

        try {
            Map<String, Double> loginMap = future.get();
            List printList = new ArrayList();
            printList.add("The total login time: " + loginMap.get("login_time"));
            printList.add("The RAD time: " + loginMap.get("rad_time"));
            printList.add("The JS time: " + loginMap.get("js_time"));
            printList.add("The log time: " + loginMap.get("log_time"));
            printList.add("The DB time: " + loginMap.get("db_time"));
            printList.add("The CPU time: " + loginMap.get("cpu_time"));
            FileUtil.writeFile(printList, outputFile);

            System.out.println();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static void runTopNQueryTask() {
        String inputFile = "e:\\sm.log";
        String outputFile = "e:\\sm_top50.log";
        TopNQueryTask task = new TopNQueryTask(50, inputFile);

        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();
        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<List<Line>> future = engine.submit(task);
        engine.shutdown();

        try {
            List<Line> lines = future.get();
            List<String> printList = new ArrayList<>();
            for (Line line : lines) {
                printList.add(line.getContent());
            }
            FileUtil.writeFile(printList, outputFile);
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

        Future<LineSetUnit> future = engine.submit(task);
        engine.shutdown();

        try {
            LineSetUnit set = future.get();
            System.out.println(set.getContent());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static void runSMOMiPerformanceTask() {
        SMOMiPerformanceTask task = new SMOMiPerformanceTask("c:\\work\\LaaS\\SMOMi.log");
        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();
        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<List<String>> future = engine.submit(task);
        engine.shutdown();

        try {
            List<String> results = future.get();
            FileUtil.writeFile(results, "C:\\work\\LaaS\\result.log");
            System.out.println("......Mission Completed");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
