package com.example.emailsenderjob.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class MailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendEmailWithAttachment(String to, String subject, String text, String pdfFilePath) throws MessagingException, IOException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        // Load PDF file
        Path path = Paths.get(pdfFilePath);
        byte[] pdfBytes = Files.readAllBytes(path);
        InputStreamSource source = new ByteArrayResource(pdfBytes);

        // Attach PDF file
        helper.addAttachment("document.pdf", source);

        // Send email
        emailSender.send(message);
    }
}
