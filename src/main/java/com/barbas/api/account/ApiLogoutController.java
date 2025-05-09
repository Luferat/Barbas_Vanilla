package com.barbas.api.account;

import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import com.barbas.core.util.JsonResponse;
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
public class ApiLogoutController {

    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;

    @GetMapping("/logout")
    public ResponseEntity<Map<String, Object>> apiAccountLogoutConfirm(HttpServletResponse response, HttpServletRequest request) {
        if (!authUtil.isLogged(request, accountRepository)) {
            return JsonResponse.error(400, "Nenhum usuário está logado no momento.");
        }

        authUtil.deleteAccountCookie(response);
        return JsonResponse.success(200, "Logout efetuado com sucesso.");
    }
}
