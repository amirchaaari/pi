package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
@Component
@Data

public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLivraison;

    private String address;

    @Temporal(TemporalType.DATE)
    private Date duration;

    @Temporal(TemporalType.DATE)
    private Date scheduleddate;


   /* @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy="livraison")
    private Set<Command> commands;*/

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;




    public enum DeliveryStatus {
        PENDING, DISPATCHED, DELIVERED
    }
}


