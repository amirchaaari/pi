package com.example.pi.service;

import com.example.pi.interfaces.trainingSession.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(String to, String verificationLink) {
        String subject = "Verify your email";
        String body = "Click the link to verify your email: " + verificationLink;
        sendPlainTextEmail(to, subject, body);
    }

    public void sendEmail(String to, String resetLink) {
        String subject = "Reset Your Password";
        String body = "Click the link to reset your password: " + resetLink;
        sendPlainTextEmail(to, subject, body);
    }

    // ✅ HTML email support
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML

        mailSender.send(message);
    }

    // 🔹 Helper for simple plain text emails
    private void sendPlainTextEmail(String to, String subject, String text) {
        var message = new org.springframework.mail.SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
