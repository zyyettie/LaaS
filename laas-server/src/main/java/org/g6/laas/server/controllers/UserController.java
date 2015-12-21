package org.g6.laas.server.controllers;

import org.g6.laas.server.database.entity.user.Quota;
import org.g6.laas.server.database.entity.user.Role;
import org.g6.laas.server.database.entity.user.User;
import org.g6.laas.server.database.repository.IRoleRepository;
import org.g6.laas.server.database.repository.IUserRepository;
import org.g6.laas.server.database.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IRoleRepository roleRepository;

    @RequestMapping(value = "/controllers/users")
    ResponseEntity<String> saveUser(@RequestBody User user) {
        User existingUser = userRepository.findByName(user.getName());
        if (existingUser != null) {
            return new ResponseEntity("{\"name\":\"" + user.getName() + "\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Role role = roleRepository.findOne(2l);
        user.setRole(role);

        Quota quota = new Quota();
        quota.setUsedSpace(524288000);
        user.setQuota(quota);

        User u = userRepository.save(user);

        return new ResponseEntity("{\"name\":\"" + u.getName() + "\"}", HttpStatus.OK);
    }
}
