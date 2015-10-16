package org.g6.laas.core.engine.task.report;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReportBuilder {

    private ViewResolver viewResolver;

    public String build(ReportModel model,String logicView){
        ReportView view = viewResolver.resolve(logicView);
        Object renderResult =  view.render(model);
        if(renderResult instanceof String)
            return (String) renderResult;
        return "";
    }
}
