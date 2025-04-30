package com.barbas.www.controller.account;

import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.BCryptUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

import static com.barbas.www.util.AuthUtil.getLoggedUser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class PasswordController {

    private final AccountRepository accountRepository;

    @PostMapping("/password/save")
    public String savePasswordEdit(
            @RequestParam String actualPassword,
            @RequestParam String newPassword1,
            @RequestParam String newPassword2,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Account> userOpt = getLoggedUser(request, accountRepository);

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Acesso negado. Faça login para continuar.");
            return "redirect:/";
        }

        Account user = userOpt.get();

        if (!user.getStatus().equals(Account.Status.ON)) {
            redirectAttributes.addFlashAttribute("error", "Conta inativa ou suspensa. Não é possível alterar a senha.");
            return "redirect:/account/edit";
        }

        if (!BCryptUtil.matches(actualPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Senha atual incorreta.");
            return "redirect:/account/edit";
        }

        if (!newPassword1.equals(newPassword2)) {
            redirectAttributes.addFlashAttribute("error", "As novas senhas não coincidem.");
            return "redirect:/account/edit";
        }

        if (newPassword1.length() < 7) {
            redirectAttributes.addFlashAttribute("error", "A nova senha deve ter pelo menos 7 caracteres.");
            return "redirect:/account/edit";
        }

        user.setPassword(BCryptUtil.encode(newPassword1));
        accountRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Senha atualizada com sucesso!");
        return "redirect:/account/profile";
    }
}