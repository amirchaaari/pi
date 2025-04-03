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



        @Column
        private String status; // validé, non validé

        @ManyToMany(cascade = CascadeType.ALL)
        @JsonIgnore
        private Set<Sport> sports;

        @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
        @JsonIgnore
        //@JsonManagedReference("club-pack")
        private Set<Pack> packs;

        @OneToOne
        @JsonIgnore
        private UserInfo owner;


}



