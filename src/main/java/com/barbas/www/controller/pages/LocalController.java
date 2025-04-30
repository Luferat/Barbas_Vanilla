package com.barbas.www.controller.pages;

import com.barbas.www.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class LocalController {

    private final Config config;

    @GetMapping("/local")
    public String local(Model model) {
        model.addAttribute("title", config.getName() + " - Onde Estamos");
        return "local";
    }
}