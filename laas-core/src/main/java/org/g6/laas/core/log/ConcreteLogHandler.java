package org.g6.laas.core.log;

import org.apache.commons.lang.StringUtils;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.filter.DefaultFilter;
import org.g6.laas.core.filter.IFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ConcreteLogHandler extends LogHandler {
    private LogFileReader reader = null;
    private Iterator<ILogFile> it = null;
    private ILogFile iLogFile = null;
    private int lineNumber;
    private AnalysisContext context;

    public ConcreteLogHandler(ILogFile iLogFile, IFilter filter) {
        super(iLogFile, filter);
    }

    public ConcreteLogHandler(List<ILogFile> list, IFilter filter) {
        super(list, filter);
    }

    public Line getNextLine() {
        try {
            if (reader == null) {
                iLogFile = getNextFile();
                if (iLogFile == null)
                    return null;

                lineNumber = 0;
                reader = new LogFileReader(iLogFile);
                reader.open();
            }

            String str;
            while ((str = reader.readLine()) != null) {
                if(StringUtils.isBlank(str))
                    continue;
                lineNumber++;
                if (getFilter() != null && getFilter().isFiltered(str))
                    continue;

                return new LogLine(iLogFile, str, lineNumber);
            }
            reader.close();
            reader = null;
        } catch (IOException e) {
            try {
                if (reader != null) {
                    reader.close();
                    reader = null;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return getNextLine();
    }

    private ILogFile getNextFile() {
        if (it == null)
            it = getList().iterator();

        if (it.hasNext())
            return it.next();

        return null;
    }

    @Override
    public Iterator<? extends Line> handle(AnalysisContext context) throws IOException {
        this.context = context;
        return iterator();
    }

    public Iterator<Line> iterator() {
        return new LogLineIterator<Line>();
    }

    private class LogLineIterator<T extends Line> implements Iterator<T> {
        private ConcreteLogHandler handler = ConcreteLogHandler.this;
        private LinkedList<T> buffer = new LinkedList<>();
        private int BUFFER_SIZE = 1024;

        @Override
        public boolean hasNext() {
            if (buffer.size() > 0)
                return true;

            fillBuffer();
            return buffer.size()>0;
        }

        @Override
        public T next() {
            fillBuffer();
            return buffer.poll();
        }

        private void fillBuffer() {
            if (buffer.size() > 0)
                return;

            for (int i=0; i<BUFFER_SIZE; i++) {
                Line line = handler.getNextLine();
                if (line == null)
                    break;
                buffer.add((T)line);
            }
        }

        @Override
        public void remove() {

        }
    }

    public static void main(String[] args) {
        List<ILogFile> list = new ArrayList<>();
        list.add(new LogFile("C:\\aa.txt"));
        list.add(new LogFile("C:\\bb.txt"));
        LogHandler handler = new ConcreteLogHandler(list, new DefaultFilter());
        try {
            Iterator<Line> it = (Iterator<Line>)handler.handle(new SimpleAnalysisContext());
            while (it.hasNext()) {
                Line line = it.next();
                System.out.println("line="+line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
