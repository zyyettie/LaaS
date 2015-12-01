package org.g6.laas.server.controllers;

import org.g6.laas.server.database.entity.user.Users;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @RequestMapping(value = "/controllers/users")
    ResponseEntity<String> saveUser(@RequestBody Users request){
        return new ResponseEntity("{\"id\":1}", HttpStatus.OK);
    }
}
