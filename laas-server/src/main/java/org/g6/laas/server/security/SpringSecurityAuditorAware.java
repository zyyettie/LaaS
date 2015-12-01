package org.g6.laas.server.security;

import org.g6.laas.server.database.entity.user.Users;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<Users> {

    @Override
    public Users getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof Users) {
                return (Users) principal;
            } else {
                Users faked = new Users(1L);
                faked.setName("admin");
                return faked;
            }
        }
        return null;
    }
}
