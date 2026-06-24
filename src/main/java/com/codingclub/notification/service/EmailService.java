package com.codingclub.notification.service;

import com.codingclub.notification.config.NotificationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private NotificationProperties properties;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendEmail(String to, String subject, String html) {
        if (properties.getEmail().getResendApiKey() == null || properties.getEmail().getResendApiKey().isBlank()) {
            log.warn("Skipping email to {} - RESEND API key not configured", to);
            return;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(properties.getEmail().getResendApiKey());

            Map<String, Object> body = new HashMap<>();
            body.put("from", properties.getEmail().getFrom());
            body.put("to", to);
            body.put("subject", subject);
            body.put("html", html);

            restTemplate.postForEntity(
                    "https://api.resend.com/emails",
                    new HttpEntity<>(body, headers),
                    String.class
            );
            log.info("Email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage());
        }
    }
}
