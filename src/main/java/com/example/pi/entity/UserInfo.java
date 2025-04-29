package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "user_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Coach.class, name = "COACH"),
        @JsonSubTypes.Type(value = Nutritionist.class, name = "NUTRITIONIST"),
        @JsonSubTypes.Type(value = ClubOwner.class, name = "ClubOwner"),
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
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Size(min = 8, message = "Password must be at least 8 characters long")

    private String password;
    @Column(nullable = false)
    private String roles;


    private Status status ;
    private LocalDateTime lastLogin = LocalDateTime.now();

    private int sessions;

    @Column
    private boolean enabled = false;

    @Column(unique = true)
    private String verificationToken;

    private LocalDateTime verificationTokenExpiry;

    @Column(unique = true)
    private String resetToken;


    @Column(name = "forcePasswordReset")
    private boolean forcePasswordReset=false;

    @Column(name = "lastPasswordChange")
    private LocalDateTime lastPasswordChange;
    private LocalDateTime tokenExpiryTime;


    private LocalDateTime lastSession;

    private String address;


    @OneToMany(cascade = CascadeType.ALL, mappedBy="coach")
    @JsonIgnore
    private Set<TrainingSession> trainingSessions;





    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    @JsonIgnore
    private Set<Review> reviews;
    // In User entity
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<DietProgram> dietPrograms;



    /*@OneToMany(mappedBy = "gymGoer", cascade = CascadeType.ALL)
    private Set<Abonnement> abonnements; // Un user peut souscrire à plusieurs abonnement*/

    // Clubs the coach is part of
    @ManyToMany(mappedBy = "coaches")
    @JsonIgnore
    private Set<Club> assignedClubs;




}
