package org.g6.laas.core.engine.task.report.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.g6.laas.core.engine.task.report.ReportView;
import org.g6.laas.core.engine.task.report.ViewResolver;
import org.g6.laas.core.engine.task.report.template.handlebars.HandlebarsReportView;
import org.g6.laas.core.exception.ViewResolveException;

import java.net.URL;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateViewResolver implements ViewResolver {

    private TemplateSystem system = TemplateSystem.HANDLEBARS;

    private String suffix = ".hbs";

    private String prefix = "/report/template/handlebars/";

    @Override
    public ReportView resolve(String name) {
        if (system.equals(TemplateSystem.HANDLEBARS)) {
            URL templateResource = getClass().getResource(prefix + name + suffix);
            return new HandlebarsReportView(templateResource.toExternalForm());
        }
        throw new ViewResolveException("Not support template engine " + system.name());
    }
}
