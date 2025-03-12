package com.example.pi.entity;

import jakarta.persistence.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxParticipants;
    private int sport;

    @ManyToOne
    private UserInfo coach;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    private List<Review> reviews;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    private List<Exercise> exercices;
}
