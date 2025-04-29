package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@DiscriminatorValue("NUTRITIONIST")
@Getter
@Setter
@AllArgsConstructor

@Builder
public class Nutritionist extends UserInfo {



}
