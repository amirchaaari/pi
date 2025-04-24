package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Booking {
    public enum Status { PENDING, APPROVED, REJECTED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Status status = Status.PENDING;
    private boolean reminderSent = false;
    @ManyToOne
    @JsonIgnore
    private UserInfo user;

    @ManyToOne
    private TrainingSession trainingSession;

    @Builder.Default
    private LocalDateTime bookedAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;
}