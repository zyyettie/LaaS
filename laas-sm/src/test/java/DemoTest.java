import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.log.BasicLogHandler;
import org.g6.laas.core.log.Line;
import org.g6.laas.core.log.LineComparator;
import org.g6.laas.core.log.LogHandler;
import org.g6.laas.core.rule.KeywordRule;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.sm.context.SMContext;
import org.g6.util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DemoTest {

    public static void main(String[] args) {
        /*String str = "REGEX:1234567";
        System.out.println(str.substring(Constants.REGEX_PREFIX.length()));
        System.out.println(RegexUtil.getValue(str, "REGEX:"));*/

        ILogFile file = new LogFile("e:\\sm.log");
        Rule rule = new KeywordRule("RTE D DBQUERY");

        LogHandler handler = new BasicLogHandler(file, rule);
        AnalysisContext context = new SMContext();
        try {
            Iterator<? extends Line> lines = handler.handle(context);
            List<Line> sortList = new ArrayList<>();

            while (lines.hasNext()) {
                Line line = lines.next();
                line.split();
                sortList.add(line);
            }
            Collections.sort(sortList, new LineComparator());

            List<String> contentList = new ArrayList<>();
            for(Line line : sortList){
               contentList.add(line.getContent());
            }

            FileUtil.writeFile(contentList, "e:\\target.log");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
