package com.barbas.core.util;

import com.barbas.core.config.Config;
import com.barbas.core.model.Account;
import com.barbas.core.repository.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class AuthUtil {

    private final Config config;

    public Optional<Account> getLoggedUser(HttpServletRequest request, AccountRepository repo) {
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

    public boolean isLogged(HttpServletRequest request, AccountRepository repo) {
        return getLoggedUser(request, repo).isPresent();
    }

    public boolean isAllowed(HttpServletRequest request, AccountRepository repo, Account.Role... allowedRoles) {
        Optional<Account> userOpt = getLoggedUser(request, repo);
        if (userOpt.isEmpty()) return false;
        Set<Account.Role> allowedSet = Set.of(allowedRoles);
        return allowedSet.contains(userOpt.get().getRole());
    }

    public void updateAccountDataCookie(HttpServletResponse response, Account account) {
        String accountDataJson = String.format(
                "{\"id\":%d, \"name\":\"%s\", \"photo\":\"%s\", \"role\":\"%s\"}",
                account.getId(),
                account.getName(),
                account.getPhoto(),
                account.getRole().name()
        );

        Cookie accountDataCookie = new Cookie(
                "accountdata",
                URLEncoder.encode(accountDataJson, StandardCharsets.UTF_8)
        );
        accountDataCookie.setMaxAge(3600 * config.getCookieHoursAge());
        accountDataCookie.setPath("/");
        accountDataCookie.setHttpOnly(false);
        accountDataCookie.setSecure(true);
        response.addCookie(accountDataCookie);
    }

    public void deleteAccountCookie(HttpServletResponse response) {
        Cookie removeCookieAccount = new Cookie("account", "");
        removeCookieAccount.setMaxAge(0);
        removeCookieAccount.setPath("/");
        removeCookieAccount.setHttpOnly(true);
        removeCookieAccount.setSecure(true);
        response.addCookie(removeCookieAccount);

        Cookie removeCookieAccountData = new Cookie("accountdata", "");
        removeCookieAccountData.setMaxAge(0);
        removeCookieAccountData.setPath("/");
        removeCookieAccountData.setHttpOnly(false);
        removeCookieAccountData.setSecure(true);
        response.addCookie(removeCookieAccountData);
    }
}
