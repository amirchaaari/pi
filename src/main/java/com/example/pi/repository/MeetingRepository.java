package com.example.pi.repository;

import com.example.pi.entity.Meeting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends CrudRepository <Meeting , Long> {


    List<Meeting> findByDateBetween(LocalDateTime start, LocalDateTime end);


    List<Meeting> findByDossierId(Long dossierId);
    long countByStatus(String status);






}
