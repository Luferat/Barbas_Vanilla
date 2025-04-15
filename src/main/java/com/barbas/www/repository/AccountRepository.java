package com.barbas.www.repository;

import com.barbas.www.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByEmail(String email);

    Optional<Account> findById(Integer id);

    boolean existsByEmail(String email);

    boolean existsByCpf(String cpf);

    Account findByEmailAndCpfAndBirth(String email, String cpf, LocalDate birthDate);
}