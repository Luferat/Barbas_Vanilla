package com.barbas.www.api.controller.service;

import com.barbas.www.model.Account;
import com.barbas.www.model.EmployeService;
import com.barbas.www.model.Service;
import com.barbas.www.repository.AccountRepository;
import com.barbas.www.repository.EmployeServiceRepository;
import com.barbas.www.repository.ServiceRepository;
import com.barbas.www.util.AuthUtil;
import com.barbas.www.util.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/service")
@RequiredArgsConstructor
public class ApiDetailController {

    private final ServiceRepository serviceRepository;
    private final EmployeServiceRepository employeServiceRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> detail(@PathVariable Integer id, HttpServletRequest request) {
        Optional<Service> opt = serviceRepository.findById(id);

        if (opt.isEmpty() || opt.get().getStatus() != Service.Status.ON) {
            return JsonResponse.error(404, "Serviço não encontrado ou indisponível");
        }

        Service service = opt.get();
        Map<String, Object> responseData = new LinkedHashMap<>();
        responseData.put("service", service);

        if (AuthUtil.isLogged(request, accountRepository)) {
            List<EmployeService> links = employeServiceRepository.findByService(service);
            List<Map<String, Object>> accounts = links.stream()
                    .map(EmployeService::getEmploye)
                    .filter(acc -> acc.getStatus() == Account.Status.ON)
                    .map(acc -> {
                        Map<String, Object> a = new LinkedHashMap<>();
                        a.put("id", acc.getId());
                        a.put("name", acc.getName());
                        a.put("photo", acc.getPhoto());
                        return a;
                    })
                    .toList();

            responseData.put("relatedAccounts", accounts);
        }

        return JsonResponse.success(200, "Serviço encontrado", responseData);
    }
}
