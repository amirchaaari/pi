package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "commands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // ❌ Remove cascade here
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @ManyToOne // ❌ Remove cascade here
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true) // ✅ Only cascade on Livraison
    private Livraison livraison;

    private Integer quantity;
}
