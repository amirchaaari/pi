package com.example.pi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoachDTO {
    private int id;
    private String name;
    private String email;
    private String roles;
    private double averageRating;
    private long reviewCount;
}
