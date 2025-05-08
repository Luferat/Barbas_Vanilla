package com.barbas.www.account;

import com.barbas.core.config.Config;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class ProfileController {

    private final Config config;
    private final AccountRepository accountRepository;

    @GetMapping("/profile")
    public String showProfile(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        return "account/profile";
    }
}
