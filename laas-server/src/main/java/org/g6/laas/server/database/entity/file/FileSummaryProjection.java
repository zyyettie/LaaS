package org.g6.laas.server.database.entity.file;

import org.springframework.data.rest.core.config.Projection;

@Projection(name = "filesummary", types = {File.class})
public interface FileSummaryProjection {

    Long getId();

    String getOriginalName();

    Long getSize();

    FileType getType();
}
