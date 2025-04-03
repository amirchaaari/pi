    package com.example.pi.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.FieldDefaults;

    import java.time.LocalDate;
    import java.time.LocalDateTime;

    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class Review {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Integer rating;

        private String description;

        private LocalDateTime createdAt = LocalDateTime.now();

        @ManyToOne
        private UserInfo user;

        @ManyToOne
        private TrainingSession trainingSession;
    }
