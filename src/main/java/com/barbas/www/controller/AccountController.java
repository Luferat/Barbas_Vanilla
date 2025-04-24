package com.barbas.www.controller;

import com.barbas.www.config.Config;
import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import com.barbas.www.util.BCryptUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.barbas.www.util.AuthUtil.getLoggedUser;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final Config config;
    private final AccountRepository accountRepository;
    private Account account;

    @PostMapping("/login")
    public String doLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpServletResponse response,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            Model model
    ) {

        // GUARD - NOT logged user only
        if (AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }

        Optional<Account> userOpt = accountRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            Account user = userOpt.get();

            if (user.getStatus().equals(Account.Status.OFF)) {
                redirectAttributes.addFlashAttribute("loginError", "Conta inativa. Entre em contato com o suporte.");
                return "redirect:/";
            } else if (user.getStatus().equals(Account.Status.DEL)) {
                redirectAttributes.addFlashAttribute("loginError", "Conta inexistente. Revise seus dados ou cadastre-se.");
                return "redirect:/";
            }

            if (BCryptUtil.matches(password, user.getPassword())) {
                Cookie loginCookie = new Cookie("account", userOpt.get().getId().toString());
                loginCookie.setMaxAge(config.getCookieHoursAge() * 60 * 60); // 24 horas
                loginCookie.setHttpOnly(true);
                loginCookie.setPath("/");
                response.addCookie(loginCookie);
                return "redirect:/";
            }
        }
        redirectAttributes.addFlashAttribute("loginError", "E-mail ou senha inválidos!");
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        return "account/profile";
    }

    @GetMapping("/edit")
    public String accountEdit(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        model.addAttribute("pageJS", "/js/account.js");
        return "account/edit";
    }

    @GetMapping("/logout")
    public String accountLogout(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        model.addAttribute("title", config.getName());
        return "account/logout";
    }

    @GetMapping("/logout/confirm")
    public String accountLogoutConfirm(HttpServletResponse response, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/"; // Redireciona se não estiver logado
        }
        AuthUtil.deleteAccountCookie(response);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String accountDelete(Model model, HttpServletRequest request) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }
        model.addAttribute("title", config.getName());
        return "account/delete";
    }

    @GetMapping("/delete/confirm")
    public String accountDeleteConfirm(HttpServletResponse response, HttpServletRequest request) {

        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }

        Optional<Account> userOpt = getLoggedUser(request, accountRepository);

        userOpt.ifPresent(account -> {
            account.setStatus(Account.Status.DEL);
            accountRepository.save(account);
        });

        AuthUtil.deleteAccountCookie(response);
        return "redirect:/";
    }

    @PostMapping("/edit/save")
    public String saveProfileEdit(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String photo,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        Optional<Account> userOpt = getLoggedUser(request, accountRepository);

        // GUARD - Logged Status.ON user only
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Acesso negado. Faça login para continuar.");
            return "redirect:/";
        }

        Account user = userOpt.get();

        try {
            user.setName(name);
            user.setEmail(email);
            user.setPhoto(photo);

            accountRepository.save(user);

            redirectAttributes.addFlashAttribute("success", "Perfil atualizado com sucesso!");
            return "redirect:/account/profile";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erro ao atualizar perfil: " + e.getMessage());
            return "redirect:/account/edit";
        }
    }

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

    @PostMapping("/photo")
    public String uploadPhoto(
            @RequestParam("file") MultipartFile file,
            Model model,
            HttpServletResponse response,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return "redirect:/";
        }

        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("photoError", "Erro! Imagem não selecionada.");
                return "redirect:/account/edit";
            }

            String contentType = file.getContentType();
            if (!List.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {
                redirectAttributes.addFlashAttribute("photoError", "Tipo de imagem não suportado.");
                return "redirect:/account/edit";
            }

            if (file.getSize() > 2 * 1024 * 1024) {
                redirectAttributes.addFlashAttribute("photoError", "Imagem muito grande. Máx: 2MB.");
                return "redirect:/account/edit";
            }

            Path uploadDir = Paths.get("uploads").toAbsolutePath();
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String filename = UUID.randomUUID() + getExtension(file.getOriginalFilename());
            Path destination = uploadDir.resolve(filename);
            file.transferTo(destination);

            account = AuthUtil.getLoggedUser(request, accountRepository).orElse(null);
            assert account != null;
            account.setPhoto("/account/uploads/" + filename);
            accountRepository.save(account);

            redirectAttributes.addFlashAttribute("success", "Imagem atualizada com sucesso!");
            return "redirect:/account/profile";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("photoError", "Erro ao salvar a imagem: " + e.getMessage());
            return "redirect:/account/edit";
        }
    }

    private String getExtension(String filename) {
        return filename != null && filename.contains(".")
                ? filename.substring(filename.lastIndexOf('.'))
                : "";
    }

    @GetMapping("/uploads/{filename:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> servePhoto(@PathVariable String filename) throws IOException {
        Path filePath = Paths.get("uploads").resolve(filename).toAbsolutePath();
        if (Files.exists(filePath)) {
            byte[] fileBytes = Files.readAllBytes(filePath);
            String contentType = Files.probeContentType(filePath);
            return ResponseEntity
                    .ok()
                    .header("Content-Type", contentType != null ? contentType : "application/octet-stream")
                    .body(fileBytes);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}