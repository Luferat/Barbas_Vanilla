package com.barbas.www.repository;

import com.barbas.www.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    // Aqui você pode adicionar métodos customizados no futuro, se quiser
}
