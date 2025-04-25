package com.barbas.www.config;

import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributes {

    private final Config config;
    private final AccountRepository accountRepository;

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("logo", config.getLogo());
        model.addAttribute("copyright", config.getCopyright());
        model.addAttribute("all", config.getAll());

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("account".equals(cookie.getName())) {
                    try {
                        Integer userId = Integer.parseInt(cookie.getValue());
                        Optional<Account> userOpt = accountRepository.findById(userId);

                        if (userOpt.isPresent() && userOpt.get().getStatus() == Account.Status.ON) {
                            model.addAttribute("loggedUser", userOpt.get());
                        } else {
                            // REMOVER COOKIE
                            Cookie removeCookie = new Cookie("account", "");
                            removeCookie.setMaxAge(0);  // Expira imediatamente
                            removeCookie.setPath("/");  // Deve ser o mesmo path usado ao criar o cookie
                            removeCookie.setHttpOnly(true);  // Opcional: manter as mesmas configurações de segurança
                            response.addCookie(removeCookie);
                        }

                    } catch (NumberFormatException ignored) {
                    }
                    break;
                }
            }
        }
    }
}