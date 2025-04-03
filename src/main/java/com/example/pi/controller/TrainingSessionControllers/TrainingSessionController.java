package com.example.pi.controller.TrainingSessionControllers;

import com.example.pi.entity.TrainingSession;
import com.example.pi.interfaces.trainingSession.ITrainingSessionService;
import com.example.pi.service.trainingSessionServices.TrainingSessionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public TrainingSession saveTrainingSession(@RequestBody TrainingSession ts) {
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

}
