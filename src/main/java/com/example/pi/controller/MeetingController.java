package com.example.pi.controller;

import com.example.pi.entity.Meeting;
import com.example.pi.service.IMeetingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/meeting")
public class MeetingController {
    IMeetingService meetingService ;
    @GetMapping("/retrieve-all-meeting")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Meeting> getMeetings() {
        List<Meeting> listClients = meetingService.retrieveAllMeetings();
        return listClients;

    }

    @GetMapping("/retrieve-meeting/{meeting-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Meeting retrieveMeeting(@PathVariable("meeting-id") Long meetingId) {
        return meetingService.retrieveMeeting(meetingId);
    }



    @PostMapping("/add-meeting")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Meeting addMeeting(@RequestBody Meeting c) {
        Meeting meeting = meetingService.addMeeting(c);
        return meeting;
    }

    @PostMapping("/add-meeting-with-dossier")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> addMeeting(@RequestParam Long dossierId, @RequestBody Meeting meeting) {
        try {
            System.out.println("dossierId: " + dossierId);
            System.out.println("Meeting Details: " + meeting);
            meetingService.createMeeting(dossierId, meeting);  // Vérifiez ici la création du meeting
            return ResponseEntity.ok("Meeting created successfully!");
        } catch (Exception e) {
            e.printStackTrace();  // Log l'erreur pour obtenir plus de détails
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while creating the meeting: " + e.getMessage());
        }
    }



    @DeleteMapping("/remove-meeting/{meeting-id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeMeeting(@PathVariable("meeting-id") Long meetingId) {
        meetingService.removeMeeting(meetingId);
    }

    @PutMapping("/update-meeting")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Meeting updateMeeting(@RequestBody Meeting c) {
        Meeting meeting= meetingService.updateMeeting(c);
        return meeting;
    }

    @GetMapping("/reminders")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Meeting> getMeetingReminders() {
        return meetingService.getMeetingReminders();
    }
    @GetMapping("/available-slots/{date}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<Map<String, Object>> getAvailableSlots(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return meetingService.getAvailableSlots(date);
    }

    @GetMapping("/by-dossier/{dossierId}")
    public List<Meeting> getMeetingsByDossier(@PathVariable Long dossierId) {
        return meetingService.getMeetingsByDossier(dossierId);
    }


    @GetMapping("/meetings")
    public Map<String, Long> getMeetingStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalMeetings", meetingService.getTotalMeetings());
        stats.put("confirmedMeetings", meetingService.getConfirmedMeetings());
        stats.put("canceledMeetings", meetingService.getCanceledMeetings());
        return stats;
    }




}
