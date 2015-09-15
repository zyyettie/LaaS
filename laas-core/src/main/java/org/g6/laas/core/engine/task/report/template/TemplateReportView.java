package org.g6.laas.core.engine.task.report.template;


import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.core.engine.task.report.StringReportView;

import java.io.File;

public abstract class TemplateReportView implements StringReportView {

    private File template;

    public TemplateReportView(File template) {
        this.template = template;
    }

    @Override
    public abstract String render(ReportModel model);
}
