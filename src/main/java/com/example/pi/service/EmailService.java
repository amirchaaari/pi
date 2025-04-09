package com.example.pi.service;

import com.example.pi.interfaces.trainingSession.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your email");
        message.setText("Click the link to verify your email: " + verificationLink);

        mailSender.send(message);
    }
}
