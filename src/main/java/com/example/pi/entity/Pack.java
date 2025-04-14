package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "packs")
public class Pack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private double price;

    @Column
    private int duration; // en mois

    @ManyToOne
    @JsonIgnore
    private Club club;

    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    //@JsonManagedReference("pack-abonnement")  // Nom explicite de la managed-reference
    private Set<Abonnement> abonnements;

    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    //@JsonManagedReference("pack-abonnement")  // Nom explicite de la managed-reference
    private Set<AbonnementRequest> abonnementsrequests;
}