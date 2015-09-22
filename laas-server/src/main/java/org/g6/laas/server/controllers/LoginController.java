package org.g6.laas.server.controllers;

import org.g6.laas.server.database.entity.user.User;
import org.g6.laas.server.database.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    @Autowired
    private IUserRepository userRepository;

    @RequestMapping(value = "/controllers/login")
    User login(@RequestBody User request) {
        User loginedUser = userRepository.findOne(1L); //hardcode TODO
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(loginedUser, loginedUser.getPassword(), loginedUser.getAuthorities());
        auth.setDetails(loginedUser.getId());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        User user = new User(1L);
        user.setName("admin");
        return user;
    }
}
