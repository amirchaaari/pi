package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TrainingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private int maxParticipants = 10;
    private int sport;
    private String meetLink;

    @ManyToOne
    @JsonIgnore
    private UserInfo coach;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Review> reviews;

    @OneToMany(mappedBy = "trainingSession", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Exercise> exercices;
}
