package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.Date;
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
    private String patientName;

    private String patientEmail;
    private String patientPhone;

    @Column(nullable = false)
    private Date birthDate; // Change de String à Date


    private String gender; // 'Male' ou 'Female'

    @Column(nullable = false)
    private String reasonForVisit; // Raisons de la visite, par exemple : perte de poids, diabète

    @Column(nullable = false)
    private String notes; // Notes liées au dossier médical

    @Column(nullable = false)
    private double weight; // Poids en kg

    @Column(nullable = false)
    private double height; // Taille en cm

    private double bmi; // Calculé côté client, optionnel ici

    private String allergies; // Allergies éventuelles

    private String medicalHistory; // Antécédents médicaux

    @Column(nullable = false)
    private Date createdAt; // Date de création

    private Date updatedAt; // Date de mise à jour (optionnelle)

    @OneToMany(cascade = CascadeType.ALL, mappedBy="dossier")
    private Set<Meeting> meetings;

}
