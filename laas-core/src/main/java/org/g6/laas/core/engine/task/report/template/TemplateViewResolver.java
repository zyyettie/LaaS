package org.g6.laas.core.engine.task.report.template;

import lombok.AllArgsConstructor;
import org.g6.laas.core.engine.task.report.ReportView;
import org.g6.laas.core.engine.task.report.ViewResolver;
import org.g6.laas.core.engine.task.report.template.handlebars.HandlebarsReportView;
import org.g6.laas.core.exception.ViewResolveException;

import java.io.File;
import java.io.FileNotFoundException;

@AllArgsConstructor
public class TemplateViewResolver implements ViewResolver {

    private TemplateLoader loader;
    private TemplateSystem system;

    @Override
    public ReportView resolve(String name) {
        try {
            File template = loader.loadTemplate(name);
            if (system.equals(TemplateSystem.HANDLEBARS))
                return new HandlebarsReportView(template);
            throw new ViewResolveException("Not support template engine " + system.name());
        } catch (FileNotFoundException e) {
            throw new ViewResolveException("Can not resolve view name \"" + name + "\"", e);
        }
    }
}
