package com.barbas.core.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private String photo = "/img/anonimous_bg_alfa.png";
    private String tel;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ON;

    @Lob
    private String metadata; // Reservado para expansões futuras

    public enum Role {
        USER, OPERATOR, EMPLOYE, ANALIST, ADMIN
    }

    public enum Status {
        ON, OFF, DEL
    }

    public String getFirstName() {
        if (this.name == null || this.name.trim().isEmpty()) {
            return "";
        }
        return this.name.split(" ")[0];
    }

    public int getAge() {
        if (this.birth == null) {
            return 0;
        }
        return Period.between(this.birth, LocalDate.now()).getYears();
    }

    public String getTextRole() {
        if (this.role == null) {
            return "Função não definida";
        }

        return switch (this.role) {
            case ADMIN -> "Administrador";
            case OPERATOR -> "Operador";
            case EMPLOYE -> "Colaborador";
            case ANALIST -> "Analista";
            case USER -> "Usuário regular";
        };
    }
}