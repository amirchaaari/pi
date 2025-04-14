package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "user_type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Coach.class, name = "COACH"),
        @JsonSubTypes.Type(value = Nutritionist.class, name = "NUTRITIONIST"),
        @JsonSubTypes.Type(value = UserInfo.class, name = "USER"),
        @JsonSubTypes.Type(value = ClubOwner.class, name = "CLUB_OWNER"),
        @JsonSubTypes.Type(value = UserInfo.class, name = "ADMIN")

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
    private int points;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="coach")
    @JsonIgnore
    private Set<TrainingSession> trainingSessions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy="user")
    @JsonIgnore
    private Set<Review> reviews;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private Set<Club> clubs; // Un user peut créer plusieurs clubs

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Abonnement> abonnements;

    @ManyToMany
    private Set<Trophy> trophies;

}
