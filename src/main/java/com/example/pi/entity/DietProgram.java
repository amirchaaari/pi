package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DietProgram")
@Getter
@Setter
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
