import org.g6.laas.core.engine.StrategyAnalysisEngine;
import org.g6.laas.core.engine.ThreadPoolExecutionStrategy;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.format.json.FormatProvider;
import org.g6.laas.core.format.json.JSONFormatProvider;
import org.g6.laas.core.log.BasicLogHandler;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.sm.task.TopNQueryTask;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class DemoTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ILogFile file = new LogFile("e:\\sm.log");
        Rule rule = new KeywordRule("RTE D DBQUERY");

        LogHandler handler = new BasicLogHandler(file, rule);
        FormatProvider provider = new JSONFormatProvider("/sm_rte_log.json");

        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.getRules().add(rule);
        context.setHandler(handler);
        context.setInputForm(provider.getInputFormat());

        TopNQueryTask task = new TopNQueryTask(context);


        StrategyAnalysisEngine engine = new StrategyAnalysisEngine();
        engine.setStrategy(new ThreadPoolExecutionStrategy());

        Future<Collection<Line>> future = engine.submit(task);
        engine.shutdown();

        List<Line> lines = (List<Line>)future.get();

        //FileUtil.writeFile(lines, "");
    }
}
