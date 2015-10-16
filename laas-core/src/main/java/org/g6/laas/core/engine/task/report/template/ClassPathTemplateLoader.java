package org.g6.laas.core.engine.task.report.template;

import java.io.File;

public class ClassPathTemplateLoader implements TemplateLoader {

    private String fileSuffix;

    public ClassPathTemplateLoader(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    @Override
    public File loadTemplate(String name) {
        return new File(getClass().getResource(name + fileSuffix).getFile());
    }
}
