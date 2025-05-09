package com.barbas.www.page;

import com.barbas.core.config.Config;
import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
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
    private final AuthUtil authUtil;

    @GetMapping("/schedule")
    public String schedule(Model model, HttpServletRequest request) {

        // GUARD - Somente usuário logado com `role.USER`, `role.EMPLOYE` ou `role.ADMIN` pode acessar
        if (!authUtil.isAllowed(request, accountRepository, Account.Role.ADMIN, Account.Role.EMPLOYE, Account.Role.USER)) {
            return "redirect:/"; // Bloqueia se não for um dos dois
        }

        model.addAttribute("title", config.getName() + " - Agendamentos");
        return "schedule";
    }
}