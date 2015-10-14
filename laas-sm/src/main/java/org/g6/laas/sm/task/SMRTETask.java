package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.file.sorter.FileSorter;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.format.provider.FormatProvider;
import org.g6.laas.core.log.handler.GenericLogHandler;
import org.g6.laas.core.log.handler.LogHandler;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.sm.file.sorter.RTELogFileSorter;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Task for SM RTE
 *
 * @param <T>
 * @author Johnson Jiang
 * @version 1.0
 */
public abstract class SMRTETask<T> extends AbstractAnalysisTask<T> {
    DefaultInputFormatProvider provider;
    private List<Rule> rules = new ArrayList<>();
    private List<String> files;

    @Override
    protected void started() {
        super.started();
        initContext();
    }

    private void initContext() {
        FormatProvider provider = getProvider();
        InputFormat inputFormat = provider.getInputFormat();

        List<ILogFile> fileList = new ArrayList<>();
        for (String file : getFiles()) {
            fileList.add(new LogFile(file));
        }

        FileSorter sorter = new RTELogFileSorter();
        LogHandler handler = new GenericLogHandler(fileList);

        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.setInputForm(inputFormat);
        context.setHandler(handler);
        context.setSorter(sorter);

        for (Rule rule : rules) {
            context.getRules().add(rule);
        }

        setContext(context);
    }

    void addRule(Rule rule) {
        rules.add(rule);
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    DefaultInputFormatProvider getProvider() {
        return getDefaultProvider();
    }

    DefaultInputFormatProvider getDefaultProvider() {
        if (provider == null) {
            provider = new DefaultInputFormatProvider("SMRTE_SM_LOG");
        } else {
            provider.setFormatKey("SMRTE_SM_LOG");
        }
        return provider;
    }
}
