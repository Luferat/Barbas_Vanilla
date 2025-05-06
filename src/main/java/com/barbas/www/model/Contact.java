package com.barbas.www.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Timestamp date;

    private String name;
    private String email;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        RECEIVED, READED, RESPONDED, ARCHIVED, DELETED
    }

    @PrePersist
    public void prePersist() {
        if (this.date == null) this.date = new Timestamp(System.currentTimeMillis());
        if (this.status == null) this.status = Status.RECEIVED;
    }
}
