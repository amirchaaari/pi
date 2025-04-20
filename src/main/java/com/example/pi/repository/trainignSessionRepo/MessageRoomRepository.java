package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.MessageRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MessageRoomRepository extends JpaRepository<MessageRoom, Integer> {

    // Find a room by the two participants (user1 and user2 in any order)
    @Query("SELECT r FROM MessageRoom r WHERE " +
            "(r.user1.id = :userId1 AND r.user2.id = :userId2) OR " +
            "(r.user1.id = :userId2 AND r.user2.id = :userId1)")
    Optional<MessageRoom> findRoomByUserIds(
            @Param("userId1") Integer userId1,
            @Param("userId2") Integer userId2
    );
}
