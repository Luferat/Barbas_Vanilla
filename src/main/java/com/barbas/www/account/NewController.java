package com.barbas.www.account;

import com.barbas.core.config.Config;
import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import com.barbas.core.util.BCryptUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class NewController {

    private final Config config;
    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;

    @GetMapping("/new")
    public String newAccount(Model model, HttpServletRequest request) {
        if (authUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }
        model.addAttribute("title", config.getName() + " - Novo usuário");
        return "account/new";
    }

    @PostMapping("/new/save")
    public String saveNewAccount(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String birth,
            @RequestParam String cpf,
            @RequestParam String password1,
            @RequestParam String password2,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (authUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // já logado
        }

        boolean emailExists = accountRepository.existsByEmail(email);
        boolean cpfExists = accountRepository.existsByCpf(cpf);

        if (emailExists || cpfExists) {
            model.addAttribute("title", config.getName() + " - Novo usuário");
            model.addAttribute("error", "Não foi possível cadastrar. "
                    + (emailExists ? "E-mail já cadastrado. " : "")
                    + (cpfExists ? "CPF já cadastrado." : ""));
            return "account/new";
        }

        if (!password1.equals(password2)) {
            model.addAttribute("title", config.getName() + " - Novo usuário");
            model.addAttribute("error", "As senhas não conferem.");
            return "account/new";
        }

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

        accountRepository.save(newAccount);

        redirectAttributes.addFlashAttribute("success", "Cadastro realizado com sucesso! Faça login para começar.");
        return "redirect:/";
    }
}
