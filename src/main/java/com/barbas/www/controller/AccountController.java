package com.barbas.www.controller;

import com.barbas.www.config.Config;
import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import com.barbas.www.util.HashUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);
    private final Config config;
    private final AccountRepository accountRepository;

    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            Model model
    ) {

        // GUARD - Somente usuários NÃO logados
        if (AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Já está logado, redireciona
        }

        Optional<Account> userOpt = accountRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Account user = userOpt.get();

            // Verifica se o status é OFF ou DEL
            if (user.getStatus().equals(Account.Status.OFF)) {
                redirectAttributes.addFlashAttribute("loginError", "Conta inativa. Entre em contato com o suporte.");
                return "redirect:/";
            } else if (user.getStatus().equals(Account.Status.DEL)) {
                redirectAttributes.addFlashAttribute("loginError", "Conta inexistente. Revise seus dados ou cadastre-se.");
                return "redirect:/";
            }

            String hashedPassword = HashUtil.sha256(password); // Criptografa a senha digitada
            if (userOpt.get().getPassword().equals(hashedPassword)) {
                Cookie loginCookie = new Cookie("account", userOpt.get().getId().toString());
                loginCookie.setMaxAge(config.getCookieHoursAge() * 60 * 60); // 24 horas
                loginCookie.setHttpOnly(true);
                loginCookie.setPath("/");
                response.addCookie(loginCookie);
                return "redirect:/";
            }
        }
        redirectAttributes.addFlashAttribute("loginError", "E-mail ou senha inválidos!");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpServletRequest request) {
        // GUARD - Somente usuário logado
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        return "account/profile";
    }

    @GetMapping("/edit")
    public String acountEdit(Model model, HttpServletRequest request) {
        // GUARD - Somente usuário logado
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        return "account/edit";
    }

    @GetMapping("/logout")
    public String accountLogout(Model model, HttpServletRequest request) {
        // GUARD - Somente usuário logado
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        return "account/logout";
    }

    @GetMapping("/logout/confirm")
    public String accountLogoutConfirm(HttpServletResponse response, HttpServletRequest request) {
        // GUARD - Somente usuário logado
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        AuthUtil.deleteAccountCookie(response);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String accountDelete(Model model, HttpServletRequest request) {
        // GUARD - Somente usuário logado
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        return "account/delete";
    }

    @GetMapping("/delete/confirm")
    public String accountDeleteConfirm(HttpServletResponse response, HttpServletRequest request) {

        // GUARD - Somente usuário logado
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }

        Optional<Account> userOpt = AuthUtil.getLoggedUser(request, accountRepository);

        // Se o usuário existe, marca como deletado
        userOpt.ifPresent(account -> {
            account.setStatus(Account.Status.DEL);
            accountRepository.save(account);
        });

        AuthUtil.deleteAccountCookie(response);
        return "redirect:/";
    }

}