package com.barbas.www.api.controller.account;

import com.barbas.www.config.Config;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import com.barbas.www.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class Logout {

    private final AccountRepository accountRepository;

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> apiAccountLogoutConfirm(HttpServletResponse response, HttpServletRequest request) {
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return JsonResponse.error(400, "Nenhum usuário está logado no momento.");
        }

        AuthUtil.deleteAccountCookie(response);
        return JsonResponse.success(200, "Logout efetuado com sucesso.");
    }
}
