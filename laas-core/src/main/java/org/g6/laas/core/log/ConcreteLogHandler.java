package org.g6.laas.core.log;

import org.apache.commons.lang.StringUtils;
import org.g6.laas.core.engine.context.AnalysisContext;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.filter.IFilter;

import java.io.IOException;
import java.util.*;

public class ConcreteLogHandler extends LogHandler {
    private LogFileReader reader = null;
    private boolean isReading = false;
    private Iterator<ILogFile> it = null;
    private ILogFile iLogFile = null;
    private AnalysisContext context;
    private int lineNumber;
    private final int TIME_INTERVAL=600000;
    private Timer timer = new Timer();
    private TimerTask tt = new TimerTask() {
        @Override
        public void run() {
            if (ConcreteLogHandler.this.isReading) {
                ConcreteLogHandler.this.isReading = false;
                return;
            }
            ConcreteLogHandler.this.close();
            timer.cancel();
        }
    };
    public ConcreteLogHandler(ILogFile iLogFile) {
        this(iLogFile, null);
    }
    public ConcreteLogHandler(ILogFile iLogFile, IFilter filter) {
        super(iLogFile, filter);
    }

    public ConcreteLogHandler(List<ILogFile> list) {
        this(list, null);
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
                lineNumber++;
                isReading = true;
                if(StringUtils.isBlank(str))
                    continue;
                if (getFilter() != null && getFilter().isFiltered(str))
                    continue;

                return new LogLine(iLogFile, str, lineNumber, context.getInputFormat());
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

    public void close() {
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Iterator<? extends Line> handle(AnalysisContext context) throws IOException {
        setContext(context);
        return iterator();
    }

    public Iterator<Line> iterator() {
        timer.schedule(tt, 0, TIME_INTERVAL);
        return new LogLineIterator<>();
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

    private void setContext(AnalysisContext context){
          this.context = context;
    }
}
