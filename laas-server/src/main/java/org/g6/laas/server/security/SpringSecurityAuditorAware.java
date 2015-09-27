package org.g6.laas.server.security;

import org.g6.laas.server.database.entity.user.User;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAware implements AuditorAware<User> {

    @Override
    public User getCurrentAuditor() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof User) {
                return (User) principal;
            } else {
                User faked = new User(1L);
                faked.setName("admin");
                return faked;
            }
        }
        return null;
    }
}
