package com.barbas.www.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final Config config;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request) {
        model.addAttribute("logo", config.getLogo());
        model.addAttribute("copyright", config.getCopyright());
        model.addAttribute("loggedUser", null);
    }
}