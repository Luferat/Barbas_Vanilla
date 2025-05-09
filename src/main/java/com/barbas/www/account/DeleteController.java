package com.barbas.www.account;

import com.barbas.core.config.Config;
import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class DeleteController {

    private final Config config;
    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;

    @GetMapping("/delete")
    public String accountDelete(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!authUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }
        model.addAttribute("title", config.getName());
        return "account/delete";
    }

    @GetMapping("/delete/confirm")
    public String accountDeleteConfirm(HttpServletResponse response, HttpServletRequest request) {

        // GUARD - Logged user only
        if (!authUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }

        Optional<Account> userOpt = authUtil.getLoggedUser(request, accountRepository);

        userOpt.ifPresent(account -> {
            account.setStatus(Account.Status.DEL);
            accountRepository.save(account);
        });

        authUtil.deleteAccountCookie(response);
        return "redirect:/";
    }
}