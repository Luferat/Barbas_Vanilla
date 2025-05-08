package com.barbas.core.repository;

import com.barbas.core.model.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findAllByStatusOrderByTitle(Service.Status status);
}
