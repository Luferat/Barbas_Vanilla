package com.barbas.www.controller.account;

import com.barbas.www.config.Config;
import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.barbas.www.util.AuthUtil.getLoggedUser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class DeleteController {

    private final Config config;
    private final AccountRepository accountRepository;

    @GetMapping("/delete")
    public String accountDelete(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }
        model.addAttribute("title", config.getName());
        return "account/delete";
    }

    @GetMapping("/delete/confirm")
    public String accountDeleteConfirm(HttpServletResponse response, HttpServletRequest request) {

        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }

        Optional<Account> userOpt = getLoggedUser(request, accountRepository);

        userOpt.ifPresent(account -> {
            account.setStatus(Account.Status.DEL);
            accountRepository.save(account);
        });

        AuthUtil.deleteAccountCookie(response);
        return "redirect:/";
    }
}