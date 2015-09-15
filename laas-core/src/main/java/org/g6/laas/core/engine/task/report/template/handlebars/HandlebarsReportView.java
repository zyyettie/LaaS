package org.g6.laas.core.engine.task.report.template.handlebars;

import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.core.engine.task.report.template.TemplateReportView;

import java.io.File;

public class HandlebarsReportView extends TemplateReportView {

    public HandlebarsReportView(File template) {
        super(template);
    }

    @Override
    public String render(ReportModel model) {
        return null;
    }
}
