package com.barbas.www.controller.pages;

import com.barbas.www.config.Config;
import com.barbas.www.model.Account;
import com.barbas.www.model.EmployeService;
import com.barbas.www.model.Service;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.repository.EmployeServiceRepository;
import com.barbas.www.repository.ServiceRepository;
import com.barbas.www.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/service")
@RequiredArgsConstructor
public class DetailController {

    private final Config config;
    private final ServiceRepository serviceRepository;
    private final EmployeServiceRepository employeServiceRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model, HttpServletRequest request) {
        Optional<Service> serviceOpt = serviceRepository.findById(id);
        if (serviceOpt.isEmpty() || serviceOpt.get().getStatus() != Service.Status.ON) {
            return "redirect:/";
        }

        Service service = serviceOpt.get();
        model.addAttribute("service", service);
        model.addAttribute("title", config.getName() + " - " + service.getTitle());

        if (AuthUtil.isLogged(request, accountRepository)) {
            List<EmployeService> links = employeServiceRepository.findByService(service);
            List<Account> relatedAccounts = links.stream()
                    .map(EmployeService::getEmploye)
                    .filter(acc -> acc.getStatus() == Account.Status.ON)
                    .toList();
            model.addAttribute("relatedAccounts", relatedAccounts);
        }

        return "service/detail";
    }
}