package org.g6.laas.core.engine.task.report.template.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.google.gson.Gson;
import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.core.engine.task.report.template.TemplateReportView;
import org.g6.laas.core.exception.ViewRenderException;
import org.g6.laas.core.exception.ViewResolveException;

import java.io.File;
import java.io.IOException;

public class HandlebarsReportView extends TemplateReportView {

    public HandlebarsReportView(File template) {
        super(template);
    }

    @Override
    public String render(ReportModel model) {

        Gson json = new Gson();
        String jsonModel =  json.toJson(model.asMap());
        Handlebars handlebars = new Handlebars();
        try{
            Template template = handlebars.compile(getTemplate().getCanonicalPath());
            return template.apply(jsonModel);
        }catch(IOException ioe){
            throw new ViewRenderException("can't apply model with template " + getTemplate().getName() + System.lineSeparator() + jsonModel);
        }
    }
}