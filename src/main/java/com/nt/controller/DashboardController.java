package com.nt.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/results")
    public String results(Model model) {
        model.addAttribute("activePage", "results");
        return "dashboard/results";
    }

    @GetMapping("/admit-cards")
    public String admitCards(Model model) {
        model.addAttribute("activePage", "admitCards");
        return "dashboard/admit-cards";
    }
}


