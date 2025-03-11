package com.example.pi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

import java.util.Set;


@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)

@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private String roles;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Club> clubs; // Un user peut créer plusieurs clubs

    @OneToMany(mappedBy = "gymGoer", cascade = CascadeType.ALL)
    private Set<Abonnement> abonnements; // Un user peut souscrire à plusieurs abonnement
}
