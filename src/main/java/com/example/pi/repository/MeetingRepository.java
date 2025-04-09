package com.example.pi.repository;

import com.example.pi.entity.Meeting;
import org.springframework.data.repository.CrudRepository;

public interface MeetingRepository extends CrudRepository <Meeting , Long> {
}
