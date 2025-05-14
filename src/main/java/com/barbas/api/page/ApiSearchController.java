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
            // Obtém os resultaos da pesquisa no banco de dados
            List<Service> services = serviceRepo.findByTitleContainingIgnoreCaseAndStatus(keyword, Service.Status.ON);
            List<Account> employes = accountRepo.findByRoleAndStatusAndNameContainingIgnoreCase(Account.Role.EMPLOYE, Account.Status.ON, keyword);

            // Obtém somente os campos úteis (e seguros) do colaborador
            List<Map<String, Object>> simpleEmployes = employes.stream().map(emp -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", emp.getId());
                map.put("name", emp.getName());
                map.put("photo", emp.getPhoto());
                map.put("role", emp.getRole());
                return map;
            }).toList();

            // Obtém uma lista dos serviços que cada colaborador executa
            Map<Integer, List<Map<String, Object>>> employeServices = new HashMap<>();
            for (Account emp : employes) {
                List<AccountService> accServices = accountServiceRepo.findByEmployeAndServiceStatus(emp, Service.Status.ON);

                // Mostra somente os dados (campos) relevantes do serviço
                List<Map<String, Object>> empServices = accServices.stream().map(as -> {
                    Service s = as.getService();
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", s.getId());
                    map.put("title", s.getTitle());
                    map.put("price", s.getPrice());
                    map.put("photo1", s.getPhoto1());
                    return map;
                }).toList();

                employeServices.put(emp.getId(), empServices);
            }

            // Filtra somente os campos necessários de cada serviço
            List<Map<String, Object>> simpleServices = services.stream().map(s -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", s.getId());
                map.put("title", s.getTitle());
                map.put("price", s.getPrice());
                map.put("photo1", s.getPhoto1());
                return map;
            }).toList();

            // Formata a chave "data" do JSON de sucesso
            Map<String, Object> result = new HashMap<>();
            result.put("services", simpleServices);
            result.put("employes", simpleEmployes);
            result.put("employeServices", employeServices);

            return JsonResponse.success(200, "Resultados da pesquisa.", result);

        } catch (Exception e) {
            return JsonResponse.error(500, "Erro ao realizar a busca: " + e.getMessage());
        }
    }
}
