package com.barbas.www.controller.page;

import com.barbas.www.config.Config;
import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ScheduleController {

    private final Config config;
    private final AccountRepository accountRepository;

    @GetMapping("/schedule")
    public String schedule(Model model, HttpServletRequest request) {

        // GUARD - Somente usuário logado com `role.USER`, `role.EMPLOYE` ou `role.ADMIN` pode acessar
        if (!AuthUtil.isAllowed(request, accountRepository, Account.Role.ADMIN, Account.Role.EMPLOYE, Account.Role.USER)) {
            return "redirect:/"; // Bloqueia se não for um dos dois
        }

        model.addAttribute("title", config.getName() + " - Agendamentos");
        return "schedule";
    }
}