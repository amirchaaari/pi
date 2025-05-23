package com.example.pi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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



    @JsonIgnore
    @OneToOne(mappedBy = "livraison", cascade = CascadeType.ALL)
    private Command command; // This must be correctly mapped

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;




    public enum DeliveryStatus {
        PENDING, DISPATCHED, DELIVERED
    }
}


