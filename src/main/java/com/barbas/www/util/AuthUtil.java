package com.barbas.www.util;

import com.barbas.www.model.Account;
import com.barbas.www.repository.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;
import java.util.Set;

public class AuthUtil {

    public static Optional<Account> getLoggedUser(HttpServletRequest request, AccountRepository repo) {
        if (request.getCookies() == null) return Optional.empty();

        for (Cookie cookie : request.getCookies()) {
            if ("account".equals(cookie.getName())) {
                try {
                    Integer userId = Integer.parseInt(cookie.getValue());
                    Optional<Account> userOpt = repo.findById(userId);
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

    public static boolean isLogged(HttpServletRequest request, AccountRepository repo) {
        return getLoggedUser(request, repo).isPresent();
    }

    public static boolean isAllowed(HttpServletRequest request, AccountRepository repo, Account.Role... allowedRoles) {
        Optional<Account> userOpt = getLoggedUser(request, repo);
        if (userOpt.isEmpty()) return false;

        Set<Account.Role> allowedSet = Set.of(allowedRoles);
        return allowedSet.contains(userOpt.get().getRole());
    }

    public static void deleteAccountCookie(HttpServletResponse response) {
        Cookie removeCookie = new Cookie("account", "");
        removeCookie.setMaxAge(0);
        removeCookie.setPath("/");
        removeCookie.setHttpOnly(true);
        removeCookie.setSecure(true);
        response.addCookie(removeCookie);
    }
}