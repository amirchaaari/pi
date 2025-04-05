package com.example.pi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "DietProgram")
public class DietProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDiet")
    private long idDiet;
    private String name;
    private String description;
    private int calories;
    private int duration; // bel ayam

}
