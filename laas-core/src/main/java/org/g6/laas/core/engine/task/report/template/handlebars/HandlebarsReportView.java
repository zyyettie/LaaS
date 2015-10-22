package org.g6.laas.core.engine.task.report.template.handlebars;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.TemplateSource;
import com.github.jknack.handlebars.io.URLTemplateSource;
import com.google.gson.Gson;
import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.core.engine.task.report.template.TemplateReportView;
import org.g6.laas.core.exception.ViewRenderException;

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
            TemplateSource templateSource = new URLTemplateSource(getTemplate(), new URL(getTemplate()));
            Template template = handlebars.compile(templateSource);
            Gson gson = new Gson();
            String json = gson.toJson(model.asMap());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(json);
            Context context = Context
                    .newBuilder(node)
                    .resolver(JsonNodeValueResolver.INSTANCE)
                    .build();
            return template.apply(context);
        } catch (IOException ioe) {
            throw new ViewRenderException("can't apply model with template " + getTemplate() + System.lineSeparator() + getTemplate());
        }
    }
}
