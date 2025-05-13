package com.barbas.api.page;

import com.barbas.core.model.Account;
import com.barbas.core.model.AccountService;
import com.barbas.core.model.Service;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.repository.AccountServiceRepository;
import com.barbas.core.repository.ServiceRepository;
import com.barbas.core.util.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ApiSearchController {

    private final ServiceRepository serviceRepo;
    private final AccountRepository accountRepo;
    private final AccountServiceRepository accountServiceRepo;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Map<String, Object>> search(@RequestParam String q) {
        String keyword = q == null ? "" : q.trim();

        if (keyword.isBlank()) {
            return JsonResponse.error(400, "O termo de busca é obrigatório.");
        }

        try {
            List<Service> services = serviceRepo.findByTitleContainingIgnoreCaseAndStatus(keyword, Service.Status.ON);
            List<Account> employes = accountRepo.findByRoleAndStatusAndNameContainingIgnoreCase(Account.Role.EMPLOYE, Account.Status.ON, keyword);

            Map<Integer, List<Service>> employeServices = new HashMap<>();
            for (Account emp : employes) {
                List<AccountService> accServices = accountServiceRepo.findByEmployeAndServiceStatus(emp, Service.Status.ON);
                List<Service> empServices = accServices.stream().map(AccountService::getService).toList();
                employeServices.put(emp.getId(), empServices);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("services", services);
            result.put("employes", employes);
            result.put("employeServices", employeServices);

            return JsonResponse.success(200, "Resultados da pesquisa.", result);

        } catch (Exception e) {
            return JsonResponse.error(500, "Erro ao realizar a busca: " + e.getMessage());
        }
    }
}
