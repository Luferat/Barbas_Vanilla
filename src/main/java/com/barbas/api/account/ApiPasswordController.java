package com.barbas.api.account;

import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import com.barbas.core.util.BCryptUtil;
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
public class ApiPasswordController {

    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;


    @PostMapping("/password")
    public ResponseEntity<Map<String, Object>> savePasswordEdit(
            @RequestParam String actualPassword,
            @RequestParam String newPassword1,
            @RequestParam String newPassword2,
            HttpServletRequest request
    ) {
        Optional<Account> userOpt = authUtil.getLoggedUser(request, accountRepository);

        if (userOpt.isEmpty()) {
            return JsonResponse.error(401, "Acesso negado. Faça login para continuar.");
        }

        Account user = userOpt.get();

        if (!user.getStatus().equals(Account.Status.ON)) {
            return JsonResponse.error(403, "Conta inativa ou suspensa. Não é possível alterar a senha.");
        }

        if (!BCryptUtil.matches(actualPassword, user.getPassword())) {
            return JsonResponse.error(400, "Senha atual incorreta.");
        }

        if (!newPassword1.equals(newPassword2)) {
            return JsonResponse.error(400, "As novas senhas não coincidem.");
        }

        if (newPassword1.length() < 7) {
            return JsonResponse.error(400, "A nova senha deve ter pelo menos 7 caracteres.");
        }

        // Atualizando a senha
        user.setPassword(BCryptUtil.encode(newPassword1));
        accountRepository.save(user);

        return JsonResponse.success(200, "Senha atualizada com sucesso.");
    }
}
