package com.barbas.www.util;

import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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
}