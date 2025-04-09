package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "meeting")
public class Meeting implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Correction ici
    private Long id;

    @Column(nullable = false)
    private LocalDateTime date;

    private String lienVisioconference;
    private String remarques;
    @ManyToOne
    private Dossier dossier;
}