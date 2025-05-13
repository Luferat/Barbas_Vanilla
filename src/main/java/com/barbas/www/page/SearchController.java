package com.barbas.www.page;

import com.barbas.core.config.Config;
import com.barbas.core.model.Account;
import com.barbas.core.model.Service;
import com.barbas.core.model.AccountService;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.repository.ServiceRepository;
import com.barbas.core.repository.AccountServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SearchController {

    private final ServiceRepository serviceRepo;
    private final AccountRepository accountRepo;
    private final AccountServiceRepository accountServiceRepo;
    private final Config config;

    @GetMapping("/search")
    public String search(
            @RequestParam("q") String keyword,
            Model model
    ) {
        List<Service> services = serviceRepo.findByTitleContainingIgnoreCaseAndStatus(keyword, Service.Status.ON);

        List<Account> employes = accountRepo.findByRoleAndStatusAndNameContainingIgnoreCase(
                Account.Role.EMPLOYE, Account.Status.ON, keyword
        );

        Map<String, List<Service>> employeServices = new HashMap<>();

        for (Account employe : employes) {
            List<Service> related = accountServiceRepo
                    .findByEmployeAndServiceStatus(employe, Service.Status.ON)
                    .stream()
                    .map(AccountService::getService)
                    .collect(Collectors.toList());

            employeServices.put(String.valueOf(employe.getId()), related);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("services", services);
        model.addAttribute("employes", employes);
        model.addAttribute("employeServices", employeServices);
        model.addAttribute("title", config.getName() + " - Pesquisando por '" + keyword + "'");

        return "search";
    }
}
