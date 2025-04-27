package com.example.pi.entity;

import com.example.pi.entity.enumeration.TargetGoal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Entity
@Table(name = "DietProgram")
@Getter
@Setter
@Component
public class DietProgram {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idDiet")
    private Long idDiet;
    private String name;
    private String description;
    private int calories;
    private int duration; // bel months
    @Enumerated(EnumType.STRING)
    private TargetGoal targetGoal;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;

    @Column(name = "creation_date")
    private LocalDate creationDate;
    @PrePersist
    public void prePersist() {
        this.creationDate = LocalDate.now();
    }


}
