package com.barbas.www.util;

import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.Set;

public class AuthUtil {

    // Retorna o usuário logado, se houver, e se status == ON
    public static Optional<Account> getLoggedUser(HttpServletRequest request, AccountRepository repo) {
        if (request.getCookies() == null) return Optional.empty();

        for (Cookie cookie : request.getCookies()) {
            if ("account".equals(cookie.getName())) {
                try {
                    Integer userId = Integer.parseInt(cookie.getValue());
                    Optional<Account> userOpt = repo.findById(userId);
                    // Retorna apenas se estiver ativo
                    if (userOpt.isPresent() && userOpt.get().getStatus() == Account.Status.ON) {
                        return userOpt;
                    }
                } catch (NumberFormatException ignored) {
                }
                break;
            }
        }

        return Optional.empty();
    }

    // Verifica se existe um usuário logado com status ON
    public static boolean isLogged(HttpServletRequest request, AccountRepository repo) {
        return getLoggedUser(request, repo).isPresent();
    }

    // Verifica se o usuário logado possui uma das roles permitidas
    public static boolean isAllowed(HttpServletRequest request, AccountRepository repo, Account.Role... allowedRoles) {
        Optional<Account> userOpt = getLoggedUser(request, repo);
        if (userOpt.isEmpty()) return false;

        Set<Account.Role> allowedSet = Set.of(allowedRoles);
        return allowedSet.contains(userOpt.get().getRole());
    }

    public static boolean deleteAccountCookie(HttpServletResponse response) {
        try {
            Cookie removeCookie = new Cookie("account", "");
            removeCookie.setMaxAge(0);  // Expira imediatamente
            removeCookie.setPath("/");  // Deve ser o mesmo path usado ao criar o cookie
            removeCookie.setHttpOnly(true);  // Melhor prática de segurança
            removeCookie.setSecure(true);  // Adicionado para uso com HTTPS
            response.addCookie(removeCookie);
            return true;  // Indica que a operação foi bem-sucedida
        } catch (Exception e) {
            // Logar a exceção seria recomendado em um ambiente real
            return false;  // Indica que a operação falhou
        }
    }
}