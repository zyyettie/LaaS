package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.engine.task.ChainTask;
import org.g6.laas.core.engine.task.TaskChain;
import org.g6.laas.core.exception.LaaSValidationException;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.file.sorter.FileSorter;
import org.g6.laas.core.file.validator.FileValidator;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.format.provider.FormatProvider;
import org.g6.laas.core.log.handler.GenericLogHandler;
import org.g6.laas.core.log.handler.LogHandler;
import org.g6.laas.core.rule.Rule;
import org.g6.laas.sm.file.sorter.RTELogFileSorter;
import org.g6.laas.sm.validator.RTELogValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SMRTETask<T> extends ChainTask<T> {
    DefaultInputFormatProvider provider;
    private List<Rule> rules = new ArrayList<>();
    private List<LogFile> files;

    @Override
    protected void started() {
        super.started();
        initRule();
        initContext();
    }

    void initRule(){}

    private void initContext() {
        FormatProvider provider = getProvider();
        InputFormat inputFormat = provider.getInputFormat();

        List<ILogFile> fileList = new ArrayList<>();
        //here file seems like a DTO that is used to encapsulate file name and originalName
        for (LogFile fileDTO : getFiles()) {
            FileValidator validator = new RTELogValidator();

            LogFile logFile = new LogFile(fileDTO.getFile(), validator);
            if(!logFile.isValid())
                throw new LaaSValidationException(logFile.getName() + " is an invalid SM RTE log file");

            fileList.add(new LogFile(fileDTO.getFile(), fileDTO.getOriginalName()));
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

    public List<LogFile> getFiles() {
        return files;
    }

    public void setFiles(List<LogFile> files) {
        this.files = files;
    }

    DefaultInputFormatProvider getProvider() {
       return getDefaultProvider(new String[]{});
    }

    DefaultInputFormatProvider getDefaultProvider(String[] names) {
        if (provider == null) {
            provider = new DefaultInputFormatProvider("SMRTE_SM_LOG");
        } else {
            provider.setFormatKey("SMRTE_SM_LOG");
        }
        provider.setNames(names);

        return provider;
    }


    @Override
    protected T process() {
        return null;
    }

    /**
     * do nothing here, this method should be overridden if the child class is used
     * to build a task workflow
     * @param request
     * @param response
     * @param chain
     */
    @Override
    public void doTask(Map request, Map response, TaskChain chain) {
    }
}
