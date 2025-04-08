package com.barbas.www.controller;

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

    @GetMapping("/about")
    public String about(Model model) {
        // Exemplo de como usar um CSS adicional nesta página / rota
        model.addAttribute("pageCSS", "/css/about.css"); // Injeta o CSS no template

        // Exemplo de como usar um Javascript adicional nesta página / rota
        model.addAttribute("pageJS", "/css/about.js"); // Injeta o Javascript no template

        model.addAttribute("title", config.getName() + " - Sobre");
        return "about";
    }
}