package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "promotions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private double discountPercentage;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate expiryDate;

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy="promotion")
    @JsonIgnore
    private Set<Category> categories;*/

    private int usageCount;
}