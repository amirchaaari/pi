package com.example.pi.repository.trainignSessionRepo;

import com.example.pi.entity.MessageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageContentRepository extends JpaRepository<MessageContent, Integer> {

    // Get all messages in a room, ordered by timestamp
    @Query("SELECT m FROM MessageContent m WHERE m.messageRoom.id = :roomId ORDER BY m.timestamp ASC")
    List<MessageContent> findAllMessagesByRoomId(@Param("roomId") int roomId);
}
