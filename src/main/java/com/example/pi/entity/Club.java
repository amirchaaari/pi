package com.example.pi.entity;
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

        @Column(nullable = false, unique = true)
        private String name;

        @Column(length = 500)
        private String description;

        @Column(nullable = false)
        private int capacity;



        @Column(nullable = false)
        private String status; // validé, non validé

        @ManyToMany(cascade = CascadeType.ALL)
        private Set<Sport> sports;

        @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
        private Set<Pack> packs;

        @ManyToOne
        private UserInfo owner; // Lien avec UserInfo


}



