package com.example.pi.entity;
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

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int duration; // en mois

    @ManyToOne
    private Club club;

    @OneToMany(mappedBy = "pack", cascade = CascadeType.ALL)
    private Set<Abonnement> abonnements;
}








