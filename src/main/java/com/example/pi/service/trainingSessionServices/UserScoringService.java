package com.example.pi.service.trainingSessionServices;

import com.example.pi.entity.UserInfo;
import com.example.pi.entity.UserLevel;
import com.example.pi.repository.UserInfoRepository;
import com.example.pi.repository.trainignSessionRepo.BookingRepository;
import com.example.pi.repository.trainignSessionRepo.ReviewRepository;
import com.example.pi.repository.trainignSessionRepo.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserScoringService {
    private final UserInfoRepository userRepo;
    private final BookingRepository bookingRepo;
    private final ReviewRepository reviewRepo;

    public void classifyUsers(UserInfo user) {
            int bookings = bookingRepo.countByUser(user);
            int reviews = reviewRepo.countByUser(user);

            double score = bookings * 1.5 + reviews * 2; // Customize weight
            user.setScore(score);
            user.setLevel(determineLevel(score));
            userRepo.save(user);
    }

    private UserLevel determineLevel(double score) {
        if (score >= 50) return UserLevel.VIP;
        if (score >= 30) return UserLevel.ENGAGED;
        if (score >= 10) return UserLevel.ACTIVE;
        return UserLevel.NEWBIE;
    }
}
