package org.g6.laas.server.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import org.g6.laas.server.database.entity.user.User;
import org.g6.laas.server.exception.InvalidUserException;
import org.springframework.security.core.context.SecurityContextHolder;

public class GenericController {

    @JsonView(User.UserDTO.class)
    public User loginedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!(principal instanceof User))
            throw new InvalidUserException("login required");
        return (User)principal;
    }
}
