package com.barbas.core.repository;

import com.barbas.core.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findById(Integer id);
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
    Account findByEmailAndCpfAndBirth(String email, String cpf, LocalDate birthDate);
    List<Account> findByRoleAndStatusAndNameContainingIgnoreCase(Account.Role role, Account.Status status, String name);
}