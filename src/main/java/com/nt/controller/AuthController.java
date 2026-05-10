package com.nt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class AuthController {


    @GetMapping("/login")
    public String login(Model model) {
        return "auth/login";
    }

    @PostMapping("/signup")
    public String signup(Model model) {
        return "auth/signup";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard/dashboard";
    }
}
