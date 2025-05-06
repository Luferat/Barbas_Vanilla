package com.barbas.www.controller.page;

import com.barbas.www.config.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PrivacyController {

    private final Config config;

    @GetMapping("/privacy")
    public String privacy(Model model) {
        model.addAttribute("title", config.getName() + " - Políticas de Privacidade");
        return "privacy";
    }
}