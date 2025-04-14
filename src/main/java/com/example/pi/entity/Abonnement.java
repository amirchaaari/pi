package com.example.pi.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "abonnements")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private String status;

    @Column
    private LocalDate endDateOfRenewal;  // Nouvelle date de fin pour le renouvellement

    @ManyToOne
    @JsonIgnore
    //@JsonBackReference("abonnement-pack")
    private Pack pack;


    @ManyToOne
    private UserInfo user; // L'abonnement est lié à un Utilisateur (Client)
}
