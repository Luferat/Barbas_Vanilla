package com.barbas.www.api.controller.account;

import com.barbas.www.config.Config;
import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import com.barbas.www.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class ApiProfileController {

    private final AccountRepository accountRepository;

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> showProfile(HttpServletRequest request) {
        Optional<Account> userOpt = AuthUtil.getLoggedUser(request, accountRepository);

        if (userOpt.isEmpty()) {
            return JsonResponse.error(401, "Usuário não está logado.");
        }

        Map<String, Object> data = Map.of(
                "id", userOpt.get().getId(),
                "name", userOpt.get().getName(),
                "email", userOpt.get().getEmail(),
                "role", userOpt.get().getRole().name(),
                "firstName", userOpt.get().getFirstName(),
                "age", userOpt.get().getAge(),
                "textRole", userOpt.get().getTextRole(),
                "photo", userOpt.get().getPhoto(),
                "birth", userOpt.get().getBirth()
        );

        return JsonResponse.success(200, "Usuário autenticado com sucesso.", data);
    }
}
