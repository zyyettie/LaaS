package org.g6.laas.server.controllers;

import org.g6.laas.server.database.entity.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @RequestMapping(value = "/controllers/login")
    ResponseEntity<String> login(@RequestBody User request){
        return new ResponseEntity("{\"id\":1}", HttpStatus.OK);
    }
}
