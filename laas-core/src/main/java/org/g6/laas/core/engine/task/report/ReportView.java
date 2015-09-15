package org.g6.laas.core.engine.task.report;

public interface ReportView<T> {
    T render(ReportModel model);
}
