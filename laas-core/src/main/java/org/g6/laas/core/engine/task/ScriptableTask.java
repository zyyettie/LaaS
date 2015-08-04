package org.g6.laas.core.engine.task;

import org.g6.laas.core.exception.LaaSCoreRuntimeException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileReader;

public abstract class ScriptableTask<T> extends AbstractAnalysisTask<T> {
    private static final ScriptEngineManager engineManager = new ScriptEngineManager();
    private ScriptEngine engine = engineManager.getEngineByName("nashorn");

    private void ScriptableTask() {
        ;
    }

    private void ScriptableTask(String engineName) {
        if (engineName != null && !"".equals(engineName)) {
            engine = engineManager.getEngineByName(engineName);
        }
    }

    private void ScriptableTask(ScriptEngine engine) {
        if (engine != null) {
            this.engine = engine;
        }
    }

    protected abstract FileReader getScript();  //new FileReader("C:\\tmp\\hello.js")

    protected T process() {
        try {
            return (T)engine.eval(getScript());
        } catch (ScriptException e) {
            throw new LaaSCoreRuntimeException("Execute script failed.");
        } catch (ClassCastException e) {
            throw new LaaSCoreRuntimeException("Cast to class from executed script failed.");
        }
    };
}
