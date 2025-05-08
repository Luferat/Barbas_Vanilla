package com.barbas.core.repository;

import com.barbas.core.model.EmployeService;
import com.barbas.core.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeServiceRepository extends JpaRepository<EmployeService, Integer> {
    List<EmployeService> findByService(Service service);
}
