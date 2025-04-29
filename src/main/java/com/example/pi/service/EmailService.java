package com.example.pi.service;

import com.example.pi.interfaces.trainingSession.IEmailService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
@Slf4j
@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private TemplateEngine templateEngine;
    private String fromEmail = "prodesigner629@gmail.com";

    public void sendVerificationEmail(String to, String verificationLink) {
        String subject = "Verify your email";
        String body = "Click the link to verify your email: " + verificationLink;
        sendPlainTextEmail(to, subject, body);
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


    public void sendDriverAssignment(String toEmail, String deliveryAddress, Long livraisonId) {
        try {
            // 1. Generate QR Code
            String confirmationUrl = "https://04ad-160-158-206-52.ngrok-free.app/delivery-confirm/" + livraisonId;
            byte[] qrCodeBytes = generateQrCodeImage(confirmationUrl);

            log.info("Sending delivery assignment to {} for Livraison ID: {}", toEmail, livraisonId);

            // 2. Create styled HTML email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("🚛 New Delivery Assignment - Action Required");

            String htmlContent = """
            <!DOCTYPE html>
            <html>
            <head>
                <style>
                    body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                    .container { max-width: 600px; margin: 20px auto; padding: 20px; border: 1px solid #e1e1e1; border-radius: 8px; }
                    .header { color: #2c3e50; border-bottom: 2px solid #f39c12; padding-bottom: 10px; }
                    .qr-container { text-align: center; margin: 20px 0; }
                    .button { 
                        display: inline-block; padding: 10px 20px; 
                        background-color: #3498db; color: white; 
                        text-decoration: none; border-radius: 4px; 
                        margin-top: 15px;
                    }
                    .footer { margin-top: 20px; font-size: 12px; color: #7f8c8d; }
                </style>
            </head>
            <body>
                <div class="container">
                    <h2 class="header">Delivery Assignment Notification</h2>
                    <p>Hello Driver,</p>
                    <p>You've been assigned a new delivery to:</p>
                    <p><strong>%s</strong></p>
                    
                    <div class="qr-container">
                        <p>Please scan this QR code to accept the delivery:</p>
                        <img src="cid:qrCode" alt="Delivery QR Code" style="width: 200px; height: 200px;"/>
                    </div>
                    
                    <p>Or click the button below:</p>
                    <a href="%s" class="button">Accept Delivery</a>
                    
                    <div class="footer">
                        <p>This is an automated message. Please do not reply directly to this email.</p>
                        <p>Delivery ID: %s</p>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(deliveryAddress, confirmationUrl, livraisonId);

            helper.setText(htmlContent, true);
            helper.addInline("qrCode", new ByteArrayResource(qrCodeBytes), "image/png");

            // 3. Send email
            mailSender.send(message);
            log.info("Delivery assignment email sent successfully to {}", toEmail);

        } catch (Exception e) {
            log.error("Failed to send delivery assignment email to {}", toEmail, e);
            throw new RuntimeException("Failed to send email with QR", e);
        }
    }

    private byte[] generateQrCodeImage(String text) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 200, 200);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);
            return out.toByteArray();
        }
    }
}