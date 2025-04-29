package com.example.pi.controller.TrainingSessionControllers;


import com.example.pi.service.trainingSessionServices.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/statistics")
@PreAuthorize("hasAnyRole('ROLE_COACH')")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/coach/bookings-by-day")
    public ResponseEntity<Map<String, Long>> getBookingsByDay() {
        return ResponseEntity.ok(statisticsService.getBookingsByDayOfWeekForCurrentCoach());
    }

    @GetMapping("/coach/bookings-by-hour")
    public ResponseEntity<Map<Integer, Long>> getBookingsByHour() {
        return ResponseEntity.ok(statisticsService.getBookingsByHourForCurrentCoach());
    }

    @GetMapping("/coach/average-rating")
    public ResponseEntity<Map<Long, Double>> getAverageRatings() {
        return ResponseEntity.ok(statisticsService.getAverageRatingPerSessionForCurrentCoach());
    }

    @GetMapping("/coach/review-count")
    public ResponseEntity<Map<Long, Long>> getReviewCounts() {
        return ResponseEntity.ok(statisticsService.getReviewCountPerSessionForCurrentCoach());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCoachStatistics() {
        return ResponseEntity.ok(statisticsService.getCoachStatistics());
    }

}
