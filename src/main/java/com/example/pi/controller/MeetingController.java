package com.example.pi.controller;

import com.example.pi.entity.Meeting;
import com.example.pi.service.IMeetingService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/meeting")
public class MeetingController {
    IMeetingService meetingService ;
    @GetMapping("/retrieve-all-meeting")
    public List<Meeting> getMeetings() {
        List<Meeting> listClients = meetingService.retrieveAllMeetings();
        return listClients;

    }


    @PostMapping("/add-meeting")
    public Meeting addMeeting(@RequestBody Meeting c) {
        Meeting meeting = meetingService.addMeeting(c);
        return meeting;
    }

    @DeleteMapping("/remove-meeting/{meeting-id}")
    public void removeMeeting(@PathVariable("meeting-id") Long meetingId) {
        meetingService.removeMeeting(meetingId);
    }

    @PutMapping("/update-meeting")
    public Meeting updateMeeting(@RequestBody Meeting c) {
        Meeting meeting= meetingService.updateMeeting(c);
        return meeting;
    }
}
