package org.g6.laas.server.controllers;

import org.g6.laas.server.database.entity.user.User;
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
    @RequestMapping(value = "/controllers/users")
    ResponseEntity<String> saveUser(@RequestBody User request){
        User existingUser = userRepository.findByName(request.getName());
        if(existingUser != null){
            return new ResponseEntity("{\"name\":\""+request.getName()+"\"}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User u = userRepository.save(request);
        return new ResponseEntity("{\"name\":\""+u.getName()+"\"}", HttpStatus.OK);
    }
}
