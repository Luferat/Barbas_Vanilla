package com.barbas.core.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employe_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EmployeService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employe_id")
    private Account employe;
}
