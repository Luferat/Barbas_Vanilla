package com.barbas.www.controller;

import com.barbas.www.config.Config;
import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import com.barbas.www.util.BCryptUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class NoAccountController {

    private final Config config;
    private final AccountRepository accountRepository;

    @GetMapping("/new")
    public String newAccount(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }
        model.addAttribute("title", config.getName() + " - Novo usuário");
        return "account/new";
    }

    @PostMapping("/new/save")
    public String saveNewAccount(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String photo,
            @RequestParam String birth,
            @RequestParam String cpf,
            @RequestParam String password1,
            @RequestParam String password2,
            HttpServletRequest request,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (AuthUtil.isLogged(request, accountRepository)) {
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
        newAccount.setPhoto(photo);
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

    @GetMapping("/password")
    public String passwordRecovery(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        if (AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }
        model.addAttribute("title", config.getName() + " - Recuperar Senha");
        return "account/password";
    }

    @PostMapping("/password/recovery")
    public String processPasswordRecovery(
            @RequestParam String birth,
            @RequestParam String email,
            @RequestParam String cpf,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        if (AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(birth);
        } catch (Exception e) {
            model.addAttribute("title", config.getName() + " - Recuperar Senha");
            model.addAttribute("error", "Data de nascimento inválida.");
            return "account/password";
        }

        Account account = accountRepository.findByEmailAndCpfAndBirth(email, cpf, birthDate);

        if (account == null) {
            model.addAttribute("title", config.getName() + " - Recuperar Senha");
            model.addAttribute("error", "Usuário não encontrado. Verifique os dados e tente novamente.");
            return "account/password";
        }

        String newPassword = generateRandomPassword();
        account.setPassword(BCryptUtil.encode(newPassword));
        accountRepository.save(account);

        model.addAttribute("title", config.getName() + " - Recuperar Senha");
        model.addAttribute("success", "Nova senha gerada com sucesso! Guarde-a com segurança e troque-a no próximo login: <code>" + newPassword + "</code>");
        return "account/password";
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
