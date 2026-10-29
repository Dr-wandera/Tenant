package com.Codeloom.Tenant.Service.Interface;

import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface EmailServiceInterface {
    void notificationToLandlordAndCaretaker( String email,  String email1, String subject,String templateName, Map<String, String> variables) throws IOException, MessagingException;

    void notificationToClient(String clientEmail, String Subject,String templateName, Map<String, String> variables) throws IOException, MessagingException;

    void sendOtpRegisterVerification( String email, String Subject,String templateName, Map<String, String> variables) throws IOException, MessagingException;

}
