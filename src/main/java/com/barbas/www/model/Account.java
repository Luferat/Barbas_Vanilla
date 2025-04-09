package com.barbas.www.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    private String photo;
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
}