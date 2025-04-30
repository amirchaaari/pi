package com.example.pi.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clubs")
public class Club {


        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column
        private String name;

        @Column
        private String description;

        @Column
        private int capacity;

        @Transient
        private int totalSubscriptions;

        @Transient
        private int totalPacks;

        @Transient
        private double averageSubscriptionsPerPack;

        @Lob
        @Column(name = "image", columnDefinition = "LONGBLOB")
        private byte[] image;




        @Enumerated(EnumType.STRING)
        private Club.RequestStatus status; // Status de la demande (APPROVED, PENDING, REJECTED)

        public enum RequestStatus {
                PENDING, APPROVED, REJECTED
        }

        @ManyToMany
        private Set<Sport> sports;

        @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
        //@JsonManagedReference("club-pack")
        private Set<Pack> packs;

        @ManyToOne
        @JsonIgnore
        private UserInfo owner;

        // 🧑‍🏫 Assigned coaches (many)
        @ManyToMany
        @JoinTable(
                name = "club_coaches",
                joinColumns = @JoinColumn(name = "club_id"),
                inverseJoinColumns = @JoinColumn(name = "user_id")
        )
        private Set<UserInfo> coaches;



}
