package com.barbas.www.repository;

import com.barbas.www.model.EmployeService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeServiceRepository extends JpaRepository<EmployeService, Integer> {
    // Ex: buscar todos os serviços de um EMPLOYE
    List<EmployeService> findByEmployeId(Integer employeId);

    // Ou buscar todos os EMPLOYES que prestam um SERVICE
    List<EmployeService> findByServiceId(Integer serviceId);
}
