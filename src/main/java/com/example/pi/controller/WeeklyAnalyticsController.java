package com.example.pi.controller;

import com.example.pi.entity.WeeklyAnalytics;
import com.example.pi.repository.WeekAnalyticsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class WeeklyAnalyticsController {

    private final WeekAnalyticsRepo weekAnalyticsRepo;

    @GetMapping("/getanalysis")
    public List<WeeklyAnalytics> getAllWeeklyAnalytics() {
        return weekAnalyticsRepo.findAll();
    }
}
