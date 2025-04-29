package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "promotions")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;
    private String description;
    private double discountPercentage;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate expiryDate;

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy="promotion")
    @JsonIgnore
    private Set<Category> categories;*/
    @Column(nullable = false)
    private boolean isActive = false; // Add this field

}