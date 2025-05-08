package com.barbas.www.account;

import com.barbas.core.config.Config;
import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import com.barbas.core.util.BCryptUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class LoginOutController {

    private final Config config;
    private final AccountRepository accountRepository;

    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {

        // GUARD - NOT logged user only
        if (AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }

        Optional<Account> userOpt = accountRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Account user = userOpt.get();

            if (user.getStatus().equals(Account.Status.OFF)) {
                redirectAttributes.addFlashAttribute("loginError", "Conta inativa. Entre em contato com o suporte.");
                return "redirect:/";
            } else if (user.getStatus().equals(Account.Status.DEL)) {
                redirectAttributes.addFlashAttribute("loginError", "Conta inexistente. Revise seus dados ou cadastre-se.");
                return "redirect:/";
            }

            if (BCryptUtil.matches(password, user.getPassword())) {
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

    @GetMapping("/logout")
    public String doLogout(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        return "account/logout";
    }

    @GetMapping("/logout/confirm")
    public String doLogoutConfirm(HttpServletResponse response, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        AuthUtil.deleteAccountCookie(response);
        return "redirect:/";
    }
}