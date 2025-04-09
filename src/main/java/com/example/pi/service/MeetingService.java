package com.example.pi.service;

import com.example.pi.entity.Meeting;
import com.example.pi.repository.MeetingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
@AllArgsConstructor
public class MeetingService implements IMeetingService{

    MeetingRepository meetingRepository ;
    @Override
    public List<Meeting> retrieveAllMeetings() {
        return (List<Meeting>) meetingRepository.findAll();
    }

    @Override
    public Meeting addMeeting(Meeting e) {
        return meetingRepository.save(e) ;
    }

    @Override
    public Meeting updateMeeting(Meeting e) {
        return meetingRepository.save(e) ;
    }

    @Override
    public Meeting retrieveMeeting(Long idMeeting) {
        return meetingRepository.findById(idMeeting).get() ;
    }

    @Override
    public void removeMeeting(Long idMeeting) {
        meetingRepository.deleteById(idMeeting);

    }

    @Override
    public List<Meeting> Meetings(List<Meeting> Meetings) {
        return (List<Meeting>) meetingRepository.saveAll(Meetings);
    }

    @Override
    public List<Meeting> findMeetingsByPatientId(Long patientId) {
        return null;
    }



    @Override
    public List<Meeting> findUpcomingMeetings(LocalDateTime fromDate) {
        return null;
    }

    @Override
    public List<Meeting> findPastMeetings(Long patientId) {
        return null;
    }

    @Override
    public double getDernierAvancement(Long patientId) {
        return 0;
    }
}
