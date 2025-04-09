package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
//@RequiredArgsConstructor
//@EqualsAndHashCode
@Builder
@Table(name = "dossier_medical")

public class Dossier implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dateNaissance;

    @Column(nullable = false)
    private double poids;

    @Column(nullable = false)
    private double taille;

    private String groupeSanguin;
    private String antecedentsMedicaux;
    private String allergies;
    private String traitementsEnCours;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="dossier")
    private Set<Meeting> meetings;

}
