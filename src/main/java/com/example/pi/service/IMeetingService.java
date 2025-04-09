package com.example.pi.service;

import com.example.pi.entity.Dossier;
import com.example.pi.entity.Meeting;
import java.time.LocalDateTime;
import java.util.List;

public interface IMeetingService {
    List<Meeting> retrieveAllMeetings();

    Meeting addMeeting(Meeting e);

    Meeting updateMeeting(Meeting e);

    Meeting retrieveMeeting (Long idMeeting);

    void removeMeeting(Long idMeeting);

    List<Meeting> Meetings (List<Meeting> Meetings);

    //les fonc avanc
    List<Meeting> findMeetingsByPatientId(Long patientId); // liste des séances d'un patient

    List<Meeting> findUpcomingMeetings(LocalDateTime fromDate); // liste les séances jeyiin
    List<Meeting> findPastMeetings(Long patientId); // liste les séances elli t3addew
    double getDernierAvancement(Long patientId); // update concerne el patient wen wsol
}
