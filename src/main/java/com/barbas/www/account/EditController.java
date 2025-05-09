package com.barbas.www.account;

import com.barbas.core.config.Config;
import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class EditController {

    private final Config config;
    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;

    @GetMapping("/edit")
    public String accountEdit(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!authUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        model.addAttribute("pageJS", "/js/account.js");
        return "account/edit";
    }

    @PostMapping("/edit/save")
    public String saveProfileEdit(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String tel,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Account> userOpt = authUtil.getLoggedUser(request, accountRepository);

        // GUARD - Logged Status.ON user only
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Acesso negado. Faça login para continuar.");
            return "redirect:/";
        }

        Account user = userOpt.get();

        try {
            user.setName(name);
            user.setEmail(email);
            user.setTel(tel);

            accountRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");
            return "redirect:/account/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar perfil: " + e.getMessage());
            return "redirect:/account/edit";
        }
    }
}