package com.example.pi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private UserInfo user1;

    @ManyToOne
    private UserInfo user2;

    @OneToMany(mappedBy = "messageRoom", cascade = CascadeType.ALL)
    private List<MessageContent> messages = new ArrayList<>();

    @Column(unique = true)
    private String uniqueRoomId; // Unique identifier for the pair (e.g., "user1Id_user2Id")

    private LocalDateTime createdAt;
}
