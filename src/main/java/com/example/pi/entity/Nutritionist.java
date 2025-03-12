package com.example.pi.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NUTRITIONIST")
public class Nutritionist extends UserInfo {

    private String specialization;
    private int experienceYears;

}
