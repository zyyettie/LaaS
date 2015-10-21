package org.g6.laas.core.engine.task.report.template.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.TemplateSource;
import com.github.jknack.handlebars.io.URLTemplateSource;
import com.google.gson.Gson;
import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.core.engine.task.report.template.TemplateReportView;
import org.g6.laas.core.exception.ViewRenderException;
import org.g6.util.JSONUtil;

import java.io.IOException;
import java.net.URL;

public class HandlebarsReportView extends TemplateReportView {

    public HandlebarsReportView(String template) {
        super(template);
    }

    @Override
    public String render(ReportModel model) {
        Handlebars handlebars = new Handlebars();
        try {
            String json = JSONUtil.toJson(model.asMap());
            URL uuu =    new URL(getTemplate());
            TemplateSource templateSource = new URLTemplateSource(getTemplate(), uuu);
            Template template = handlebars.compile(templateSource);

            Context context = Context
                    .newBuilder(model.asMap())
                    .resolver(MapValueResolver.INSTANCE)
                    .build();
            return template.apply(context);
        } catch (IOException ioe) {
            throw new ViewRenderException("can't apply model with template " + getTemplate() + System.lineSeparator() + getTemplate());
        }
    }
}
