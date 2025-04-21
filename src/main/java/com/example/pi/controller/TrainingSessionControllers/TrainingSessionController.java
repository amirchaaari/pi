package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.dto.CoachDTO;
import com.example.pi.entity.TrainingSession;
import com.example.pi.service.trainingSessionServices.TrainingSessionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/training-sessions")
public class TrainingSessionController {

    @Autowired
    TrainingSessionService trainingSessionService;

    @GetMapping("/retrieve-all-TrainingSessions")
    public List<TrainingSession> getTrainingSessions() {
        return trainingSessionService.getAllSessions();
    }

    @GetMapping("/retrieve-TrainingSession/{idTrainingSession}")
    public TrainingSession retrieveTrainingSession(@PathVariable("idTrainingSession") Long id) {
        return trainingSessionService.getSessionById(id);
    }

    @PostMapping("/add-TrainingSession")
    @PreAuthorize("hasRole('ROLE_COACH')")
    public TrainingSession saveTrainingSession(@RequestBody TrainingSession ts) throws Exception {
        return trainingSessionService.createSession(ts);
    }

    @PutMapping("/update-TrainingSession")
    @PreAuthorize("hasRole('ROLE_COACH')")
    public TrainingSession updateTrainingSession(@RequestBody TrainingSession ts) {
        return trainingSessionService.updateSession(ts.getId(), ts);
    }

    @DeleteMapping("/delete-TrainingSession/{idTrainingSession}")
    @PreAuthorize("hasRole('ROLE_COACH')")
    public void deleteTrainingSession(@PathVariable("idTrainingSession") Long id) {
        trainingSessionService.deleteSession(id);
    }

    @GetMapping("/recommended")
    public ResponseEntity<List<CoachDTO>> getRecommendedCoaches() {
        List<CoachDTO> coaches = trainingSessionService.getRecommendedCoaches();
        return ResponseEntity.ok(coaches);
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<TrainingSession>> getSessionsInRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<TrainingSession> sessions = trainingSessionService.getSessionsInRange(start, end);
        return ResponseEntity.ok(sessions);
    }



}
