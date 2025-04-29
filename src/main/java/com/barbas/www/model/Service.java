package com.barbas.www.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private LocalDateTime date = LocalDateTime.now();

    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;

    @Lob
    private String metadata;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ON;

    public enum Status {
        ON, OFF
    }
}
