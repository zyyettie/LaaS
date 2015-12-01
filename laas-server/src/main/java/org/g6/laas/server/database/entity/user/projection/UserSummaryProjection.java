package org.g6.laas.server.database.entity.user.projection;

import org.g6.laas.server.database.entity.user.Users;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "usersummary", types = { Users.class })
public interface UserSummaryProjection {
    Long getId();
    String getName();
}
