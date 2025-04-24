package com.example.pi.service;

import com.example.pi.entity.Dossier;
import com.example.pi.entity.Meeting;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface IMeetingService {
    List<Meeting> retrieveAllMeetings();

    Meeting addMeeting(Meeting e);

    Meeting updateMeeting(Meeting e);

    Meeting retrieveMeeting(Long idMeeting);


    void removeMeeting(Long idMeeting);

    List<Meeting> Meetings (List<Meeting> Meetings);






    List<Meeting> getMeetingReminders();


    List<Map<String, Object>> getAvailableSlots(LocalDate date);
}
