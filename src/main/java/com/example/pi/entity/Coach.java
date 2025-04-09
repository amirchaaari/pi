package com.example.pi.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@DiscriminatorValue("COACH")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Coach extends UserInfo {

    private String sport;
    private int experienceYears;
    private String certifications;


}
