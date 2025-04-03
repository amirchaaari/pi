package com.example.pi.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CLUB_OWNER") // Differentiates it in the database
public class ClubOwner extends UserInfo {

    private String clubName;
    private String businessRegistration;

}
