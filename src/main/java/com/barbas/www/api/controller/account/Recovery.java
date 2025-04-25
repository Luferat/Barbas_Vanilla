package com.barbas.www.api.controller.account;

import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import com.barbas.www.util.BCryptUtil;
import com.barbas.www.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class Recovery {

    private final AccountRepository accountRepository;

    @PostMapping("/recovery")
    public ResponseEntity<Map<String, Object>> processPasswordRecovery(
            @RequestParam String birth,
            @RequestParam String email,
            @RequestParam String cpf,
            HttpServletRequest request) {

        // Verifica se o usuário já está logado
        if (AuthUtil.isLogged(request, accountRepository)) {
            return JsonResponse.error(400, "Usuário já está logado.");
        }

        // Valida a data de nascimento
        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birth);
        } catch (Exception e) {
            return JsonResponse.error(400, "Data de nascimento inválida.");
        }

        // Busca conta
        Account account = accountRepository.findByEmailAndCpfAndBirth(email, cpf, birthDate);
        if (account == null) {
            return JsonResponse.error(404, "Usuário não encontrado. Verifique os dados e tente novamente.");
        }

        // Gera nova senha
        String newPassword = generateRandomPassword();
        account.setPassword(BCryptUtil.encode(newPassword));
        accountRepository.save(account);

        // Retorna nova senha em texto (responsabilidade do frontend exibir com segurança)
        Map<String, Object> data = Map.of("newPassword", newPassword);
        return JsonResponse.success(200, "Nova senha gerada com sucesso!", data);

    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        int length = random.nextInt(4) + 7; // entre 7 e 10
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
