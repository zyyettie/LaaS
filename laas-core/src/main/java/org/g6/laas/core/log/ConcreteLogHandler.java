package org.g6.laas.core.log;

import org.g6.laas.LaaSContext;
import org.g6.laas.core.field.Field;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.rule.Rule;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhouzhan
 * Date: 6/18/15
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConcreteLogHandler extends LogHandler {
    public ConcreteLogHandler(ILogFile iLogFile, Rule rule) {
        super(iLogFile, rule);
    }

    public ConcreteLogHandler(List<ILogFile> list, Rule rule) {
        super(list, rule);
    }

    @Override
    public Iterator<? extends Slice> handle(LaaSContext context) throws IOException {
        Collection<Line> collection = new ArrayList<>();
        for (ILogFile iLogFile : list) {
            LogFileReader reader = new LogFileReader(iLogFile);
            reader.open();
            String str;
            int number = 0;
            while ((str = reader.readLine()) != null) {
                number++;
                if (rule.isSatisfied(str)) {
                    Line line = new LogLine(iLogFile, str, number, context.isSplitable());
                    collection.add(line);
                }
            }

        }
        return collection.iterator();
    }
}
