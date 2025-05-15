package com.barbas.core.config;

import com.barbas.core.model.Account;
import com.barbas.core.model.AccountService;
import com.barbas.core.model.Service;
import com.barbas.core.repository.AccountRepository;
import com.barbas.core.repository.ServiceRepository;
import com.barbas.core.repository.AccountServiceRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final AccountRepository accountRepository;
    private final ServiceRepository serviceRepository;
    private final AccountServiceRepository employeServiceRepository;

    @PostConstruct
    public void init() {
        if (accountRepository.count() > 0) return;

        String encryptedPassword = new BCryptPasswordEncoder().encode("Senha123");

        // Criando todas as contas
        Account account1 = new Account(null, LocalDateTime.now().minusDays(350), "/photo/1.jpg", "(21) 99887-7665", LocalDate.of(2000, 11, 12), "Joca da Silva", "joca@email.com", "999.888.777-66", encryptedPassword, Account.Role.ADMIN, Account.Status.ON, null);
        Account account2 = new Account(null, LocalDateTime.now().minusDays(340), "/photo/11.jpg", "(21) 98765-4321", LocalDate.of(1984, 8, 30), "Marineuza Siriliano", "marineuza@email.com", "888.777.666-55", encryptedPassword, Account.Role.OPERATOR, Account.Status.ON, null);
        Account account3 = new Account(null, LocalDateTime.now().minusDays(330), "/photo/2.jpg", "(21) 98989-7676", LocalDate.of(1992, 1, 24), "Dilermano Souza", "dilermano@email.com", "777.666.555-44", encryptedPassword, Account.Role.EMPLOYE, Account.Status.ON, null);
        Account account4 = new Account(null, LocalDateTime.now().minusDays(320), "/photo/3.jpg", "(21) 98877-6665", LocalDate.of(2001, 3, 29), "Setembrino Trocatapas", "setembrino@email.com", "666.555.444-33", encryptedPassword, Account.Role.ANALIST, Account.Status.ON, null);
        Account account5 = new Account(null, LocalDateTime.now().minusDays(310), "/photo/12.jpg", "(21) 98798-7987", LocalDate.of(1981, 9, 20), "Hemengarda Sirigarda", "hemengarda@email.com", "555.444.333-22", encryptedPassword, Account.Role.USER, Account.Status.DEL, null);
        Account account6 = new Account(null, LocalDateTime.now().minusDays(300), "/photo/4.jpg", "(21) 99988-8777", LocalDate.of(1981, 9, 20), "Fernandino Nomecladastio", "fernandino@email.com", "444.333.222-11", encryptedPassword, Account.Role.USER, Account.Status.OFF, null);
        Account account7 = new Account(null, LocalDateTime.now().minusDays(290), "/photo/13.jpg", "(21) 98798-7997", LocalDate.of(1981, 9, 20), "Salestiana Correntina", "salestiana@email.com", "333.222.111-00", encryptedPassword, Account.Role.USER, Account.Status.ON, null);
        Account account8 = new Account(null, LocalDateTime.now().minusDays(280), "/photo/15.jpg", "(21) 91111-2222", LocalDate.of(1990, 7, 12), "Zuleica da Navalha", "zuleica@email.com", "222.111.000-99", encryptedPassword, Account.Role.EMPLOYE, Account.Status.ON, null);
        Account account9 = new Account(null, LocalDateTime.now().minusDays(270), "/photo/6.jpg", "(21) 92222-3333", LocalDate.of(1987, 5, 3), "Brunildo dos Cortes", "brunildo@email.com", "333.222.111-88", encryptedPassword, Account.Role.EMPLOYE, Account.Status.ON, null);
        Account account10 = new Account(null, LocalDateTime.now().minusDays(260), "/photo/17.jpg", "(21) 93333-4444", LocalDate.of(1995, 12, 9), "Clarice Estilosa", "clarice@email.com", "444.333.222-77", encryptedPassword, Account.Role.EMPLOYE, Account.Status.ON, null);
        Account account11 = new Account(null, LocalDateTime.now().minusDays(250), "/photo/8.jpg", "(21) 94444-5555", LocalDate.of(1993, 10, 18), "Genivaldo Barbeiro", "genivaldo@email.com", "555.444.333-66", encryptedPassword, Account.Role.EMPLOYE, Account.Status.ON, null);

        // Salvando todas as contas
        List<Account> accounts = List.of(account1, account2, account3, account4, account5, account6, account7, account8, account9, account10, account11);
        accountRepository.saveAll(accounts);

        // Criando os serviços
        Service service1 = new Service(null, LocalDateTime.now().minusDays(77), "Corte simples", "Corte tradicional com tesoura e pente", new BigDecimal("29.90"), "https://picsum.photos/400/300", "https://picsum.photos/401/301", "https://picsum.photos/402/302", "https://picsum.photos/403/303", "", Service.Status.ON);
        Service service2 = new Service(null, LocalDateTime.now().minusDays(66), "Corte com máquina", "Rápido, prático e moderno com máquina elétrica", new BigDecimal("25.00"), "https://picsum.photos/400/300", "https://picsum.photos/401/301", "https://picsum.photos/402/302", "https://picsum.photos/403/303", "", Service.Status.ON);
        Service service3 = new Service(null, LocalDateTime.now().minusDays(55), "Corte + Barba + Sobrancelha", "Pacote completo com estilo", new BigDecimal("44.00"), "https://picsum.photos/400/300", "https://picsum.photos/401/301", "https://picsum.photos/402/302", "https://picsum.photos/403/303", "", Service.Status.ON);
        Service service4 = new Service(null, LocalDateTime.now(), "Corte degradê", "Corte estilizado com acabamento degradê", new BigDecimal("39.90"), "https://picsum.photos/400/300", "https://picsum.photos/401/301", "https://picsum.photos/402/302", "https://picsum.photos/403/303", "", Service.Status.ON);
        Service service5 = new Service(null, LocalDateTime.now(), "Hidratação capilar", "Tratamento nutritivo para cabelos masculinos", new BigDecimal("35.00"), "https://picsum.photos/400/300", "https://picsum.photos/401/301", "https://picsum.photos/402/302", "https://picsum.photos/403/303", "", Service.Status.ON);
        Service service6 = new Service(null, LocalDateTime.now(), "Barba desenhada", "Barba com desenho personalizado e navalha", new BigDecimal("30.00"), "https://picsum.photos/400/300", "https://picsum.photos/401/301", "https://picsum.photos/402/302", "https://picsum.photos/403/303", "", Service.Status.ON);
        Service service7 = new Service(null, LocalDateTime.now(), "Sobrancelha na régua", "Acabamento preciso na sobrancelha com régua e navalha", new BigDecimal("20.00"), "https://picsum.photos/400/300", "https://picsum.photos/401/301", "https://picsum.photos/402/302", "https://picsum.photos/403/303", "", Service.Status.ON);
        Service service8 = new Service(null, LocalDateTime.now(), "Luzes no cabelo", "Clareamento parcial com estilo", new BigDecimal("59.90"), "https://picsum.photos/400/300", "https://picsum.photos/401/301", "https://picsum.photos/402/302", "https://picsum.photos/403/303", "", Service.Status.ON);

        // Salvando todos os serviços
        List<Service> services = List.of(service1, service2, service3, service4, service5, service6, service7, service8);
        serviceRepository.saveAll(services);

        // Criando os relacionamentos entre empregados e serviços
        // Corte simples atendido por Zuleica (id 8) e Genivaldo (id 11)
        employeServiceRepository.save(new AccountService(null, service1, account8));
        employeServiceRepository.save(new AccountService(null, service1, account11));

        // Corte com máquina por Brunildo (id 9) e Clarice (id 10)
        employeServiceRepository.save(new AccountService(null, service2, account9));
        employeServiceRepository.save(new AccountService(null, service2, account10));

        // Pacote completo por todos os novos EMPLOYE
        employeServiceRepository.save(new AccountService(null, service3, account8));
        employeServiceRepository.save(new AccountService(null, service3, account9));
        employeServiceRepository.save(new AccountService(null, service3, account10));
        employeServiceRepository.save(new AccountService(null, service3, account11));

        // Corte degradê: Clarice e Genivaldo
        employeServiceRepository.save(new AccountService(null, service4, account10));
        employeServiceRepository.save(new AccountService(null, service4, account11));

        // Hidratação capilar: Zuleica e Clarice
        employeServiceRepository.save(new AccountService(null, service5, account8));
        employeServiceRepository.save(new AccountService(null, service5, account10));

        // Barba desenhada: Brunildo e Genivaldo
        employeServiceRepository.save(new AccountService(null, service6, account9));
        employeServiceRepository.save(new AccountService(null, service6, account11));

        // Sobrancelha na régua: Zuleica e Brunildo
        employeServiceRepository.save(new AccountService(null, service7, account8));
        employeServiceRepository.save(new AccountService(null, service7, account9));

        // Luzes no cabelo: Clarice
        employeServiceRepository.save(new AccountService(null, service8, account10));
    }
}