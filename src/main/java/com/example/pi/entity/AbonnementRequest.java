package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "abonnement_requests")
public class AbonnementRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserInfo user;

    @ManyToOne
    @JsonIgnore
    private Pack pack;
    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;


    @Enumerated(EnumType.STRING)
    private RequestStatus status; // PENDING, APPROVED, REJECTED

    private LocalDate requestedDate;

    public enum RequestStatus {
        PENDING, APPROVED, REJECTED
    }
}
