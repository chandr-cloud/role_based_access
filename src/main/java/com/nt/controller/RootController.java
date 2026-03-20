package com.nt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String index() {
        return "<a href='/swagger-ui/index.html'>Go to Swagger UI</a>";
    }
}