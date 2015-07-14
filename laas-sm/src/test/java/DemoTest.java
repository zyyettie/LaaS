import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.log.Line;
import org.g6.laas.sm.task.LoginTimeInfoTask;
import org.g6.laas.sm.task.TopNQueryTask;
import org.g6.util.Constants;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DemoTest {

    public static void main(String[] args) {
        runLoginTimeInfoTask();
    }

    static void runLoginTimeInfoTask(){
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
}
