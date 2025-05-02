package com.barbas.www.api.controller.account;

import com.barbas.www.config.Config;
import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/account")
public class ApiPhotoController {

    private final AccountRepository accountRepository;
    private final Config config;

    @PostMapping("/photo")
    public ResponseEntity<Object> uploadPhoto(
            @RequestParam("file") MultipartFile file,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        // GUARD - Logged user only
        if (!AuthUtil.isLogged(request, accountRepository)) {
            return ResponseEntity
                    .status(401)
                    .body(Map.of("message", "Acesso negado. Faça login para continuar."));
        }

        try {
            if (file.isEmpty()) {
                return ResponseEntity
                        .status(400)
                        .body(Map.of("message", "Erro! Imagem não selecionada."));
            }

            String contentType = file.getContentType();
            if (!List.of("image/jpeg", "image/png", "image/webp").contains(contentType)) {
                return ResponseEntity
                        .status(400)
                        .body(Map.of("message", "Tipo de imagem não suportado."));
            }

            if (file.getSize() > 2 * 1024 * 1024) {
                return ResponseEntity
                        .status(400)
                        .body(Map.of("message", "Imagem muito grande. Máx: 2MB."));
            }

            Path uploadDir = Paths.get("uploads").toAbsolutePath();
            if (Files.notExists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            String filename = UUID.randomUUID() + getExtension(file.getOriginalFilename());
            Path destination = uploadDir.resolve(filename);
            file.transferTo(destination);

            Account account = AuthUtil.getLoggedUser(request, accountRepository).orElse(null);
            assert account != null;
            account.setPhoto("/account/uploads/" + filename);
            accountRepository.save(account);

            // Atualiza o cookie 'accountdata' com a nova foto e role
            AuthUtil.updateAccountDataCookie(response, account);

            return ResponseEntity
                    .status(200)
                    .body(Map.of("message", "Imagem atualizada com sucesso!", "photoUrl", "/account/uploads/" + filename));

        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body(Map.of("message", "Erro ao salvar a imagem: " + e.getMessage()));
        }
    }

    private String getExtension(String filename) {
        return filename != null && filename.contains(".")
                ? filename.substring(filename.lastIndexOf('.'))
                : "";
    }

    private void updateAccountDataCookie(HttpServletResponse response, Account account) {
        // Cria o cookie 'accountdata' com a foto atualizada
        String accountDataJson = "{\"id\":" + account.getId() + ", \"name\":\"" + account.getName() + "\", \"photo\":\"" + account.getPhoto() + "\"}";

        Cookie accountDataCookie = new Cookie("accountdata", accountDataJson);
        accountDataCookie.setMaxAge(config.getCookieHoursAge() * 60 * 60); // Cookie expira em 1 dia
        accountDataCookie.setPath("/"); // Torna o cookie acessível em todo o domínio
        accountDataCookie.setHttpOnly(false); // Permite acesso via JavaScript no front-end
        accountDataCookie.setSecure(true); // Se estiver usando HTTPS, garante que o cookie seja seguro
        response.addCookie(accountDataCookie);
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
            return ResponseEntity
                    .status(404)
                    .body(null);
        }
    }
}
