package org.g6.laas.core.engine.task.report.template;

import java.io.File;

public interface TemplateLoader {
    File loadTemplate(String name);
}
