package com.example.pi.service.trainingSessionServices;

import com.example.pi.config.HmsConfig;
import com.example.pi.entity.Booking;
import com.example.pi.entity.TrainingSession;
import com.example.pi.entity.UserInfo;
import com.example.pi.interfaces.trainingSession.ITrainingSessionService;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.BookingRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Service
@RequiredArgsConstructor
public class TrainingSessionService implements ITrainingSessionService {
    @Autowired
    private final TrainingSessionRepository trainingSessionRepository;
    @Autowired
    private final UserInfoRepository userInfoRepository;
    @Autowired
    private final HmsService hmsService;
    @Autowired
    private final BookingRepository bookingRepository;
    private final HmsConfig hmsConfig;

    @Override
    public TrainingSession createSession(TrainingSession trainingSession) {
        //jib l email mtaa l user l connecté b token mteouu
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();


        // njybou l coach m database
        UserInfo coach = userInfoRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Coach not found with email: " + currentUserEmail));

        // naffectiwah l trainingsession
        trainingSession.setCoach(coach);
        String roomId = hmsService.createRoom(trainingSession);
        System.out.println(roomId);
        String meetLink = hmsService.generateMeetingLink(roomId);
        trainingSession.setHmsRoomId(roomId);
        trainingSession.setHmsRoomLink(meetLink);
        if (trainingSession.getStartTime().isAfter(trainingSession.getEndTime())) {
            throw new RuntimeException("Start time must be before end time");
        }

        return trainingSessionRepository.save(trainingSession);
    }


    @Override
    public TrainingSession updateSession(Long id, TrainingSession trainingSession) {
        trainingSession.setId(id);
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

