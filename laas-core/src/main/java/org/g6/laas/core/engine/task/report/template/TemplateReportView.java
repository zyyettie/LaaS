package org.g6.laas.core.engine.task.report.template;


import lombok.Data;
import org.g6.laas.core.engine.task.report.ReportModel;
import org.g6.laas.core.engine.task.report.StringReportView;

import java.io.File;

@Data
public abstract class TemplateReportView implements StringReportView {

    private String template;

    public TemplateReportView(String template) {
        this.template = template;
    }

    @Override
    public abstract String render(ReportModel model);
}
