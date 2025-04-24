package com.example.pi.interfaces.trainingSession;

import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;

import java.util.List;

public interface ITrainingSessionService {
    TrainingSession createSession(TrainingSession session) throws Exception;
    List<TrainingSession> getAllSessions();
    TrainingSession getSessionById(Long sessionId);
    TrainingSession updateSession(Long id,TrainingSession session);
    void deleteSession(Long sessionId);
}
