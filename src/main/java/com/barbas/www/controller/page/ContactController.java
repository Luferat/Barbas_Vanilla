package com.barbas.www.controller.page;

import com.barbas.www.config.Config;
import com.barbas.www.model.Contact;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.repository.ContactRepository;
import com.barbas.www.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ContactController {

    private final Config config;
    private final ContactRepository contactRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/contact")
    public String contact(Model model, HttpServletRequest request) {
        model.addAttribute("title", config.getName() + " - Faça Contato");
        model.addAttribute("pageCSS", "/css/contact.css");
        AuthUtil.getLoggedUser(request, accountRepository).ifPresent(account -> {
            model.addAttribute("name", account.getName());
            model.addAttribute("email", account.getEmail());
        });
        return "contact";
    }

    @PostMapping("/contact")
    public String sendContact(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        try {
            Contact contact = Contact.builder()
                    .name(name.trim())
                    .email(email.trim().toLowerCase())
                    .subject(subject.trim())
                    .message(message.trim())
                    .build();

            contactRepository.save(contact);

            model.addAttribute("success", "Contato enviado com sucesso!");
            model.addAttribute("title", config.getName() + " - Faça Contato");
            model.addAttribute("pageCSS", "/css/contact.css");
            AuthUtil.getLoggedUser(request, accountRepository).ifPresent(account -> {
                model.addAttribute("name", account.getName());
                model.addAttribute("email", account.getEmail());
            });
            return "contact";

        } catch (Exception e) {
            model.addAttribute("error", "Erro ao enviar contato: " + e.getMessage());
            return "contact";
        }
    }
}
