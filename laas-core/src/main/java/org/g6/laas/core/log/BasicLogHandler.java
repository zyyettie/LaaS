package org.g6.laas.core.log;

import lombok.extern.slf4j.Slf4j;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.filter.IFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class BasicLogHandler extends LogHandler {

    public BasicLogHandler(ILogFile iLogFile, IFilter filter) {
        super(iLogFile, filter);
    }

    public BasicLogHandler(List<ILogFile> list, IFilter filter) {
        super(list, filter);
    }

    private Collection<ILogFile> validateLogFiles(Collection<ILogFile> lofFiles) {
        Collection<ILogFile> list = new ArrayList<>();
        for (ILogFile iLogFile : lofFiles) {
            if (iLogFile.isValid()) {
                list.add(iLogFile);
            } else {
                log.warn(iLogFile.getName() + " maybe contain incorrect format. Skip it.");
            }
        }
        return list;
    }

    @Override
    public Iterator<? extends Line> handle(AnalysisContext context) throws IOException {
        Collection<Line> collection = new ArrayList<>();

        Collection<ILogFile> logFiles = validateLogFiles(getList());
        for (ILogFile iLogFile : logFiles) {
            LogFileReader reader = new LogFileReader(iLogFile);
            reader.open();
            String str;
            int number = 0;
            while ((str = reader.readLine()) != null) {
                number++;
                if (getFilter() != null) {
                    if (!getFilter().isFiltered(str)) {
                        Line line = new LogLine(iLogFile, str, number);
                        collection.add(line);
                    }
                } else {
                    Line line = new LogLine(iLogFile, str, number);
                    collection.add(line);
                }
            }
            reader.close();
        }
        return collection.iterator();
    }
}
