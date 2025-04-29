package com.example.pi.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GoogleOAuthConfig {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    // Method to authorize Google OAuth
    public Credential authorize() throws Exception {
        JsonFactory jsonFactory = new JacksonFactory();
        GoogleClientSecrets.Details details = new GoogleClientSecrets.Details();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setRedirectUris(List.of(redirectUri));

        GoogleClientSecrets clientSecrets = new GoogleClientSecrets().setWeb(details);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                jsonFactory,
                clientSecrets,
                List.of(CalendarScopes.CALENDAR)
        ).setAccessType("offline")
                .setApprovalPrompt("force")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(8090)
                .setCallbackPath("/oauth2/callback")
                .build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
    public Calendar getCalendarService() throws Exception {
        Credential credential = authorize();
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                new JacksonFactory(),
                credential)
                .setApplicationName("Google Calendar API Integration")
                .build();
    }
}
