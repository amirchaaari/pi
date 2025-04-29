package com.example.pi.repository;

import com.example.pi.entity.WeeklyAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeekAnalyticsRepo extends JpaRepository<WeeklyAnalytics, Long> {
}
