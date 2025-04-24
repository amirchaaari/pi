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
@NoArgsConstructor
@Builder
public class Nutritionist extends UserInfo {






    private String phone;
    private Double salary;

    @Temporal(TemporalType.DATE)
    private Date hiredDate;


}
