package com.barbas.api.account;

import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import com.barbas.core.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
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
public class ApiEditController {

    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;


    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> saveProfileEdit(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String tel,
            HttpServletRequest request
    ) {
        Optional<Account> userOpt = authUtil.getLoggedUser(request, accountRepository);

        // GUARD - Logged Status.ON user only
        if (userOpt.isEmpty()) {
            return JsonResponse.error(401, "Acesso negado. Faça login para continuar.");
        }

        Account user = userOpt.get();

        try {
            user.setName(name);
            user.setEmail(email);
            user.setTel(tel);

            accountRepository.save(user);

            Map<String, Object> data = Map.of(
                    "id", user.getId(),
                    "name", user.getName(),
                    "email", user.getEmail(),
                    "role", user.getRole().name(),
                    "firstName", user.getFirstName(),
                    "age", user.getAge(),
                    "textRole", user.getTextRole(),
                    "photo", user.getPhoto(),
                    "birth", userOpt.get().getBirth(),
                    "tel", userOpt.get().getTel()
            );

            return JsonResponse.success(200, "Perfil atualizado com sucesso.", data);
        } catch (Exception e) {
            return JsonResponse.error(500, "Erro ao atualizar perfil: " + e.getMessage());
        }
    }
}
