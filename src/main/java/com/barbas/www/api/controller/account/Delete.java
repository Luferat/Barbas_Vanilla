package com.barbas.www.api.controller.account;

import com.barbas.www.model.Account;
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
import java.util.Optional;

import static com.barbas.www.util.AuthUtil.getLoggedUser;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class Delete {

    private final AccountRepository accountRepository;

    @GetMapping("/delete")
    public ResponseEntity<Map<String, Object>> accountDeleteConfirm(HttpServletResponse response, HttpServletRequest request) {

        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return JsonResponse.error(401, "Usuário não está logado.");
        }

        Optional<Account> userOpt = getLoggedUser(request, accountRepository);

        if (userOpt.isPresent()) {
            Account user = userOpt.get();
            // Marca a conta como deletada
            user.setStatus(Account.Status.DEL);
            accountRepository.save(user);

            // Remove o cookie "account"
            AuthUtil.deleteAccountCookie(response);

            // Retorna sucesso com mensagem
            return JsonResponse.success(200, "Conta apagada com sucesso.");
        } else {
            // Caso não encontre o usuário
            return JsonResponse.error(404, "Usuário não encontrado.");
        }
    }
}
