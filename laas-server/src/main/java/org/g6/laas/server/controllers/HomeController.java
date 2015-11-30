package org.g6.laas.server.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @RequestMapping(
            value = {
                    "/jobs/**",
                    "/tasks/**",
                    "/user/**",
                    "/scenarios/**",
                    "/files/**",
                    "/job",
                    "/jobs/**",
                    "/jobHistory/**",
                    "/jobRunnings/**",
                    "/home",
                    "/login",
                    "jobnew",
                    "files/me",
                    "notifications/me"
            },
            method = RequestMethod.GET)
    public String index() {
        return "index.html";
    }
}
