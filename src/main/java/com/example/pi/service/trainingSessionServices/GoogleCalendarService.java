package com.example.pi.service.trainingSessionServices;

import com.example.pi.config.GoogleOAuthConfig;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GoogleCalendarService {

    @Autowired
    private GoogleOAuthConfig googleOAuthConfig;

    public String createEvent(String title, String description, String startTime, String endTime) throws Exception {
        // Get the authorized Calendar service instance
        Calendar service = googleOAuthConfig.getCalendarService();
        if (service == null) {
            throw new Exception("Failed to create Calendar service instance.");
        }

        // Create a new event
        Event event = new Event()
                .setSummary(title)
                .setDescription(description);

        // Set the event start and end time
        DateTime start = new DateTime(startTime);
        event.setStart(new EventDateTime().setDateTime(start));

        DateTime end = new DateTime(endTime);
        event.setEnd(new EventDateTime().setDateTime(end));

        // Add Google Meet link
        ConferenceData conferenceData = new ConferenceData()
                .setCreateRequest(new CreateConferenceRequest()
                        .setRequestId(UUID.randomUUID().toString())
                        .setConferenceSolutionKey(new ConferenceSolutionKey().setType("hangoutsMeet")));

        event.setConferenceData(conferenceData);

        // Insert the event into the user's primary calendar
        Event createdEvent = service.events().insert("primary", event)
                .setConferenceDataVersion(1)
                .execute();

        // Return the Google Meet link
        return createdEvent.getHangoutLink();
    }
}
