package com.Codeloom.Tenant.EmailConfiguration;

import com.Codeloom.Tenant.Service.Interface.EmailServiceInterface;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImplementation implements EmailServiceInterface {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void notificationToLandlordAndCaretaker(String landlordEmail, String caretakerEmail, String subject, String templateName, Map<String, String> variables) throws IOException, MessagingException {
        String templatePath = "Template/" + templateName ;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath);

        if (inputStream == null) {
            throw new FileNotFoundException("Email template not found: " + templatePath);
        }

        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            htmlContent = htmlContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(new String[]{landlordEmail, caretakerEmail});
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    //sends email to client after booking a room
    @Async
    @Override
    public void notificationToClient(String clientEmail, String Subject, String templateName, Map<String, String> variables) throws IOException, MessagingException {
        String templatePath = "Template/" + templateName ;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath);

        if (inputStream == null) {
            throw new FileNotFoundException("Email template not found: " + templatePath);
        }

        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            htmlContent = htmlContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(clientEmail);
        helper.setSubject(Subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    //sent otp to registered user
    @Async
    @Override
    public void sendOtpRegisterVerification(String email, String Subject, String templateName, Map<String, String> variables) throws IOException, MessagingException {
        String templatePath = "Template/" + templateName ;

        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templatePath);

        if (inputStream == null) {
            throw new FileNotFoundException("Email template not found: " + templatePath);
        }

        String htmlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        for (Map.Entry<String, String> entry : variables.entrySet()) {
            htmlContent = htmlContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject(Subject);
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }
}
