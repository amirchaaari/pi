package com.example.pi.dto;

import com.example.pi.entity.enumeration.TargetGoal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class DietProgramRequest {

    private Long idDiet;
    private String name;
    private String description;
    private int calories;
    private int duration; // en mois
    private TargetGoal targetGoal;
    private LocalDate creationDate;
    private String userUsername;
    private String userEmail;

}
