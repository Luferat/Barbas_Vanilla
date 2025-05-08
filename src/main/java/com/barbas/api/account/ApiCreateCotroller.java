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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class ApiCreateCotroller {

    private final AccountRepository accountRepository;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> saveNewAccount(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String birth,
            @RequestParam String cpf,
            @RequestParam String password1,
            @RequestParam String password2,
            @RequestParam String tel,
            HttpServletRequest request) {

        // Verifica se o usuário já está logado
        if (AuthUtil.isLogged(request, accountRepository)) {
            return JsonResponse.error(400, "Usuário já está logado.");
        }

        // Verifica se o e-mail ou CPF já estão cadastrados
        boolean emailExists = accountRepository.existsByEmail(email);
        boolean cpfExists = accountRepository.existsByCpf(cpf);

        if (emailExists || cpfExists) {
            return JsonResponse.error(400, "Não foi possível cadastrar. "
                    + (emailExists ? "E-mail já cadastrado. " : "")
                    + (cpfExists ? "CPF já cadastrado." : ""));
        }

        // Verifica se as senhas coincidem
        if (!password1.equals(password2)) {
            return JsonResponse.error(400, "As senhas não conferem.");
        }

        // Criação do novo usuário
        Account newAccount = new Account();
        newAccount.setName(name);
        newAccount.setEmail(email);
        newAccount.setPhoto("/photo/anonimous.png");
        newAccount.setCpf(cpf);
        newAccount.setBirth(LocalDate.parse(birth));
        newAccount.setPassword(BCryptUtil.encode(password1));
        newAccount.setCreatedAt(LocalDateTime.now());
        newAccount.setStatus(Account.Status.ON);
        newAccount.setRole(Account.Role.USER);
        newAccount.setTel(tel);

        // Salva o novo usuário no banco
        accountRepository.save(newAccount);

        // Retorna sucesso
        return JsonResponse.success(200, "Cadastro realizado com sucesso! Faça login para começar.");
    }
}
