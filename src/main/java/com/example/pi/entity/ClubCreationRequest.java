package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ClubCreationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserInfo clubOwner;

    private String name;
    private String description;
    private int capacity;
    @Lob
    @Column(name = "document", columnDefinition = "LONGBLOB")
    private byte[] document;

    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;


    @Enumerated(EnumType.STRING)
    private RequestStatus status; // Status de la demande (APPROVED, PENDING, REJECTED)

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED
    }
}
