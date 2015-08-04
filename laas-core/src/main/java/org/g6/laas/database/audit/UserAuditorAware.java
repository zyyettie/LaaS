package org.g6.laas.database.audit;

import org.g6.laas.database.entity.User;
import org.springframework.data.domain.AuditorAware;

public class UserAuditorAware implements AuditorAware<User> {
    @Override
    public User getCurrentAuditor() {
        return null;
    }
}
