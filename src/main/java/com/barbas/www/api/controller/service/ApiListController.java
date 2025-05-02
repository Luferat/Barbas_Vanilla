package com.barbas.www.api.controller.service;

import com.barbas.www.model.Service;
import com.barbas.www.repository.ServiceRepository;
import com.barbas.www.util.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/service")
public class ApiListController {

    private final ServiceRepository serviceRepository;

    @GetMapping("/all")
    public ResponseEntity<?> listAllServices() {
        try {
            List<Service> services = serviceRepository.findAllByStatusOrderByTitle(Service.Status.ON);
            return JsonResponse.success(200, "Serviços ativos encontrados", services);
        } catch (Exception e) {
            return JsonResponse.error(500, "Erro ao buscar serviços", e.getMessage());
        }
    }
}
