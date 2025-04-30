package com.barbas.www.controller.pages;

import com.barbas.www.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final Config config;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", config.getName());
        return "home";
    }
}