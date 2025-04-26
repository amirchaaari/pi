
package com.example.pi.service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class EmailService  {

    private final JavaMailSender mailSender;

    public void sendDriverAssignment(String toEmail, String deliveryAddress) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("New Delivery Assignment");
            message.setText(
                    "You've been assigned a new delivery:\n\n" +
                            "Delivery Address: " + deliveryAddress + "\n\n" +
                            "Please respond with:\n" +
                            "ACCEPT [deliveryId] to accept\n" +
                            "DECLINE [deliveryId] to decline"
            );

            mailSender.send(message);
        } catch (MailException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

