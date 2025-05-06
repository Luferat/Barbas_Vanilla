package com.barbas.www.api.controller.page;

import com.barbas.www.model.Contact;
import com.barbas.www.repository.ContactRepository;
import com.barbas.www.util.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ApiContactController {

    private final ContactRepository contactRepository;

    // Recebe JSON (application/json)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> sendJsonContact(@RequestBody Map<String, String> body) {
        return handleContact(
                body.get("name"),
                body.get("email"),
                body.get("subject"),
                body.get("message")
        );
    }

    // Recebe formulário (application/x-www-form-urlencoded)
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Map<String, Object>> sendFormContact(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String subject,
            @RequestParam String message
    ) {
        return handleContact(name, email, subject, message);
    }

    // Lógica centralizada
    private ResponseEntity<Map<String, Object>> handleContact(String name, String email, String subject, String message) {
        try {
            if (isBlank(name) || isBlank(email) || isBlank(subject) || isBlank(message)) {
                return JsonResponse.error(400, "Todos os campos são obrigatórios.");
            }

            Contact contact = Contact.builder()
                    .name(name.trim())
                    .email(email.trim().toLowerCase())
                    .subject(subject.trim())
                    .message(message.trim())
                    .build();

            contactRepository.save(contact);

            return JsonResponse.success(200, "Contato enviado com sucesso.");

        } catch (Exception e) {
            return JsonResponse.error(500, "Erro ao enviar contato: " + e.getMessage());
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
