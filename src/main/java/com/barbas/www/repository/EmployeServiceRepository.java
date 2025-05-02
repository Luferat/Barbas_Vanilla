package com.barbas.www.repository;

import com.barbas.www.model.EmployeService;
import com.barbas.www.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeServiceRepository extends JpaRepository<EmployeService, Integer> {
    List<EmployeService> findByService(Service service);
}
