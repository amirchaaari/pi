package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.trainingSession.ITrainingSessionService;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class TrainingSessionService implements ITrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public TrainingSession createSession(TrainingSession trainingSession) {
        UserInfo coach = userInfoRepository.findById(trainingSession.getCoach().getId()).orElse(null);
        trainingSession.setCoach(coach);
        TrainingSession session = trainingSessionRepository.save(trainingSession);
        return session;
    }

    @Override
    public TrainingSession updateSession(Long id, TrainingSession trainingSession) {
        trainingSession.setId(id); // Set the ID explicitly
        return trainingSessionRepository.save(trainingSession);
    }

    @Override
    public void deleteSession(Long id) {
        trainingSessionRepository.deleteById(id);
    }

    @Override
    public TrainingSession getSessionById(Long id) {
        return trainingSessionRepository.findById(id).orElse(null);
    }

    @Override
    public List<TrainingSession> getAllSessions() {
        return trainingSessionRepository.findAll();
    }
}

