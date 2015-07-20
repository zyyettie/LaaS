package org.g6.laas.sm.task;

import org.g6.laas.core.engine.context.SimpleAnalysisContext;
import org.g6.laas.core.engine.task.AbstractAnalysisTask;
import org.g6.laas.core.file.ILogFile;
import org.g6.laas.core.file.LogFile;
import org.g6.laas.core.format.InputFormat;
import org.g6.laas.core.format.provider.DefaultInputFormatProvider;
import org.g6.laas.core.format.provider.FormatProvider;
import org.g6.laas.core.log.handler.ConcreteLogHandler;
import org.g6.laas.core.log.handler.LogHandler;
import org.g6.laas.core.rule.Rule;

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

    protected void initContext(String file, Rule rule) {
        initContext(file, new Rule[]{rule});
    }

    protected void initContext(String[] files, Rule rule) {
        initContext(files, new Rule[]{rule});
    }

    protected void initContext(String file, Rule[] rules) {
        initContext(new String[]{file}, rules);
    }


    protected void initContext(String[] files, Rule[] rules) {
        FormatProvider provider = getProvider();
        InputFormat inputFormat = provider.getInputFormat();

        List<ILogFile> fileList = new ArrayList<>();
        for (String file : files) {
            fileList.add(new LogFile(file));
        }
        LogHandler handler = new ConcreteLogHandler(fileList);

        SimpleAnalysisContext context = new SimpleAnalysisContext();
        context.setInputForm(inputFormat);
        context.setHandler(handler);

        for (Rule rule : rules) {
            context.getRules().add(rule);
        }

        setContext(context);
    }

    FormatProvider getProvider() {
        if (provider == null) {
            provider = new DefaultInputFormatProvider("SMRTE_SM_LOG");
        } else {
            provider.setFormatKey("SMRTE_SM_LOG");
        }
        return provider;
    }
}
