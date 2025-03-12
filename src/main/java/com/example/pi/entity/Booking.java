package com.example.pi.entity;

import jakarta.persistence.Id;
import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.FieldDefaults;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String status;

    @ManyToOne
    UserInfo user;

    @ManyToOne
    TrainingSession trainingSession;
}