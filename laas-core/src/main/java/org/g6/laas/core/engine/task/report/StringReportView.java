package org.g6.laas.core.engine.task.report;

public interface StringReportView extends ReportView<String> {
    @Override
    String render(ReportModel model);
}
