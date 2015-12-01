package org.g6.laas.server.database.audit;

import org.g6.laas.server.database.entity.user.Users;
import org.springframework.data.domain.AuditorAware;

public class UserAuditorAware implements AuditorAware<Users> {
    @Override
    public Users getCurrentAuditor() {
        return null;
    }
}
