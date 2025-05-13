package com.barbas.core.repository;

import com.barbas.core.model.Account;
import com.barbas.core.model.AccountService;
import com.barbas.core.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountServiceRepository extends JpaRepository<AccountService, Integer> {
    List<AccountService> findByService(Service service);
    List<AccountService> findByEmployeAndServiceStatus(Account employe, Service.Status status);
}
