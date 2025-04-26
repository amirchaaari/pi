package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "user_type"
)
@JsonSubTypes({


        @JsonSubTypes.Type(value = UserInfo.class, name = "USER"),

        @JsonSubTypes.Type(value = UserInfo.class, name = "ADMIN"),
        @JsonSubTypes.Type(value = UserInfo.class, name = "UserInfo"),

})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String email;
    private String password;
    private String roles;


    private Status status = Status.ONLINE;
    private LocalDateTime lastLogin = LocalDateTime.now();

//    @OneToMany(cascade = CascadeType.ALL, mappedBy="coach")
//    @JsonIgnore
//    private Set<TrainingSession> trainingSessions;
//
//    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
//    @JsonIgnore
//    private Set<Review> reviews;


}
