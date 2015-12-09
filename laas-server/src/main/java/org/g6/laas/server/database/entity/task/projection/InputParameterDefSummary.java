package org.g6.laas.server.database.entity.task.projection;

import org.g6.laas.server.database.entity.task.InputParameterDef;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "inputParameterDefSummary", types = { InputParameterDef.class })
public interface InputParameterDefSummary {
    Long getId();
    String getName();
    String getDisplayInfo();
    String getDescription();
    String getDefaultValue();
    String getType();
}
