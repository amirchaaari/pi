package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "livreurs")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Livreur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;


    private String email;


    private String phone;


    private String address; // Their base location


    private boolean available = true;

    // No relation to Livraison to maintain independence
}
