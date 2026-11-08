package com.Codeloom.Tenant.EmailConfiguration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class MailConfiguration {
    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.sender-name}")
    private String senderName;

}
