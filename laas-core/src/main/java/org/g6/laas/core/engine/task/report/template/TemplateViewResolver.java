package org.g6.laas.core.engine.task.report.template;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.g6.laas.core.engine.task.report.ClassPathTemplateLoader;
import org.g6.laas.core.engine.task.report.ReportView;
import org.g6.laas.core.engine.task.report.ViewResolver;
import org.g6.laas.core.engine.task.report.template.handlebars.HandlebarsReportView;
import org.g6.laas.core.exception.ViewResolveException;

import java.io.File;

@AllArgsConstructor
@NoArgsConstructor
public class TemplateViewResolver implements ViewResolver {

    private TemplateLoader loader = new ClassPathTemplateLoader(".hbs");
    private TemplateSystem system = TemplateSystem.HANDLEBARS;

    @Override
    public ReportView resolve(String name) {
        File template = loader.loadTemplate(name);
        if (system.equals(TemplateSystem.HANDLEBARS))
            return new HandlebarsReportView(template);
        throw new ViewResolveException("Not support template engine " + system.name());
    }
}
