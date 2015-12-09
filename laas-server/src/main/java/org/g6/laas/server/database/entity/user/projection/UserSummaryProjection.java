package org.g6.laas.server.database.entity.user.projection;

import org.g6.laas.server.database.entity.user.User;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "usersummary", types = { User.class })
public interface UserSummaryProjection {
    Long getId();
    String getName();
}
