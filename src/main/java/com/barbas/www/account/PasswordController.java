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

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;

import static com.barbas.core.util.AuthUtil.getLoggedUser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class PasswordController {

    private final Config config;
    private final AccountRepository accountRepository;

    @PostMapping("/password/save")
    public String savePasswordEdit(
            @RequestParam String actualPassword,
            @RequestParam String newPassword1,
            @RequestParam String newPassword2,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Account> userOpt = getLoggedUser(request, accountRepository);

        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Acesso negado. Faça login para continuar.");
            return "redirect:/";
        }

        Account user = userOpt.get();

        if (!user.getStatus().equals(Account.Status.ON)) {
            redirectAttributes.addFlashAttribute("error", "Conta inativa ou suspensa. Não é possível alterar a senha.");
            return "redirect:/account/edit";
        }

        if (!BCryptUtil.matches(actualPassword, user.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Senha atual incorreta.");
            return "redirect:/account/edit";
        }

        if (!newPassword1.equals(newPassword2)) {
            redirectAttributes.addFlashAttribute("error", "As novas senhas não coincidem.");
            return "redirect:/account/edit";
        }

        if (newPassword1.length() < 7) {
            redirectAttributes.addFlashAttribute("error", "A nova senha deve ter pelo menos 7 caracteres.");
            return "redirect:/account/edit";
        }

        user.setPassword(BCryptUtil.encode(newPassword1));
        accountRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Senha atualizada com sucesso!");
        return "redirect:/account/profile";
    }

    @GetMapping("/password")
    public String passwordRecovery(Model model, HttpServletRequest request) {
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
            HttpServletRequest request) {

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