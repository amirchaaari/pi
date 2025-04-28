package com.example.pi.service;

import com.example.pi.entity.Meeting;
import com.example.pi.repository.DossierRepository;
import com.example.pi.repository.MeetingRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.example.pi.entity.Dossier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.List;

@Service
@AllArgsConstructor
public class MeetingService implements IMeetingService{

    MeetingRepository meetingRepository ;
    DossierRepository dossierRepository;

    public Meeting createMeeting(Long dossierId, Meeting meeting) {
        Dossier dossier = dossierRepository.findById(dossierId)
                .orElseThrow(() -> new RuntimeException("Dossier not found"));

        meeting.setDossier(dossier);

        System.out.println(">>> DEBUG: Dossier ID = " + meeting.getDossier().getId());  // Ajoute ce log pour vérifier
        return meetingRepository.save(meeting);
    }

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
        return meetingRepository.findById(idMeeting).orElse(null);
    }


    @Override
    public void removeMeeting(Long idMeeting) {
        meetingRepository.deleteById(idMeeting);

    }

    @Override
    public List<Meeting> Meetings(List<Meeting> Meetings) {
        return (List<Meeting>) meetingRepository.saveAll(Meetings);
    }




    private List<Meeting> meetingReminders = new ArrayList<>();

    @Scheduled(cron = "0/15 * * * * *") // chaque 30 min
    public void sendMeetingReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime inOneHour = now.plusHours(1);

        List<Meeting> meetingsToRemind = meetingRepository.findByDateBetween(now, inOneHour);
        meetingReminders = meetingsToRemind; // mettre à jour la liste

        for (Meeting meeting : meetingsToRemind) {
            String message = "Reminder : Meeting with " + meeting.getPatientName()
                    + " at " + meeting.getDate().toString();
            System.out.println(message);
        }
    }

    public List<Map<String, Object>> getAvailableSlots(LocalDate date) {
        List<Map<String, Object>> slots = new ArrayList<>();
        LocalTime start = LocalTime.of(9, 0);
        LocalTime end = LocalTime.of(17, 0);

        // Créneaux toutes les 30 minutes
        while (start.isBefore(end)) {
            Map<String, Object> slot = new HashMap<>();
            slot.put("time", start.toString());

            LocalDateTime slotDateTime = date.atTime(start);

            boolean isBooked = meetingRepository.findByDateBetween(
                    slotDateTime,
                    slotDateTime.plusMinutes(29) // demi-heure
            ).size() > 0;

            slot.put("available", !isBooked);
            slots.add(slot);

            start = start.plusMinutes(30);
        }

        return slots;
    }















    @Override
    public List<Meeting> getMeetingReminders() {
        return meetingReminders;
    }

@Override
    public List<Meeting> getMeetingsByDossier(Long dossierId) {
        return meetingRepository.findByDossierId(dossierId);
    }

    public long getTotalMeetings() {
        return meetingRepository.count();
    }

    public long getConfirmedMeetings() {
        return meetingRepository.countByStatus("Confirmed");
    }

    public long getCanceledMeetings() {
        return meetingRepository.countByStatus("Canceled");
    }
}
