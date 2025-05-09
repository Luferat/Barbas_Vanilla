package com.barbas.www.account;

import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/account")
public class PhotoController {

    private final AccountRepository accountRepository;
    private final AuthUtil authUtil;

    @PostMapping("/photo")
    public String uploadPhoto(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes
    ) {
        // GUARD - Logged user only
        if (!authUtil.isLogged(request, accountRepository)) {
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

            Account account = authUtil.getLoggedUser(request, accountRepository).orElse(null);
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