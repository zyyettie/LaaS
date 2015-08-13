package org.g6.laas.server.database.audit;

import org.g6.laas.server.database.entity.user.User;
import org.springframework.data.domain.AuditorAware;

public class UserAuditorAware implements AuditorAware<User> {
    @Override
    public User getCurrentAuditor() {
        return null;
    }
}
