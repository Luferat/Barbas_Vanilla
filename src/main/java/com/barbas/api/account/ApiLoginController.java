package com.barbas.api.account;

import com.barbas.core.config.Config;
import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import com.barbas.core.util.BCryptUtil;
import com.barbas.core.util.JsonResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class ApiLoginController {

    private final AccountRepository accountRepository;
    private final Config config;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> apiLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        if (AuthUtil.isLogged(request, accountRepository)) {
            return JsonResponse.error(400, "Usuário já está logado.");
        }

        Optional<Account> userOpt = accountRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Account user = userOpt.get();

            if (user.getStatus().equals(Account.Status.OFF)) {
                return JsonResponse.error(403, "Conta inativa. Entre em contato com o suporte.");
            } else if (user.getStatus().equals(Account.Status.DEL)) {
                return JsonResponse.error(404, "Conta não encontrada. Verifique os dados ou cadastre-se.");
            }

            if (BCryptUtil.matches(password, user.getPassword())) {
                // Cria cookie seguro com ID do usuário
                Cookie loginCookie = new Cookie("account", user.getId().toString());
                loginCookie.setMaxAge(config.getCookieHoursAge() * 60 * 60);
                loginCookie.setHttpOnly(true);
                loginCookie.setPath("/");
                response.addCookie(loginCookie);

                // Cria o cookie 'accountdata' com o role, nome e foto
                AuthUtil.updateAccountDataCookie(response, user);

                return JsonResponse.success(200, "Login efetuado com sucesso.");
            }
        }

        return JsonResponse.error(401, "E-mail ou senha inválidos.");
    }
}
