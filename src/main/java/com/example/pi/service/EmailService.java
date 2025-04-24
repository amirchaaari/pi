package com.example.pi.service;

import com.example.pi.interfaces.trainingSession.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    private String fromEmail = "prodesigner629@gmail.com";

    public void sendVerificationEmail(String to, String verificationLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verify your email");
        message.setText("Click the link to verify your email: " + verificationLink);

        mailSender.send(message);
    }
    public void sendBookingConfirmationEmail(String toEmail, String sessionDescription,
                                             LocalDate sessionDate, LocalTime startTime) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Training Session Booking Confirmation");

            Context context = new Context();
            context.setVariable("sessionDescription", sessionDescription);
            context.setVariable("sessionDate", sessionDate);
            context.setVariable("startTime", startTime);
            String htmlContent = templateEngine.process("BookingConfirmation", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send confirmation email: " + e.getMessage());
        }
    }

    public void sendBookingRejectionEmail(String toEmail, String sessionDescription) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Training Session Booking Rejection");

            Context context = new Context();
            context.setVariable("sessionDescription", sessionDescription);
            String htmlContent = templateEngine.process("BookingRejection", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send rejection email: " + e.getMessage());
        }
    }

    public void sendReminderEmail(String toEmail, String sessionDescription,
                                  LocalDate sessionDate, LocalTime startTime, String meetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("Training Session Reminder");

            Context context = new Context();
            context.setVariable("sessionDescription", sessionDescription);
            context.setVariable("sessionDate", sessionDate);
            context.setVariable("startTime", startTime);
            context.setVariable("meetLink", meetLink);
            String htmlContent = templateEngine.process("Reminder", context);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Failed to send reminder email: " + e.getMessage());
        }
    }
}