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
import java.util.List;
import java.util.Map;

@Service
public class DiscordService {

    private static final Logger log = LoggerFactory.getLogger(DiscordService.class);

    @Autowired
    private NotificationProperties properties;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendEmbed(String webhookUrl, String title, String description, int color, List<Map<String, Object>> fields) {
        if (webhookUrl == null || webhookUrl.isBlank()) {
            log.warn("Skipping Discord notification - webhook not configured");
            return;
        }

        try {
            Map<String, Object> embed = new HashMap<>();
            embed.put("title", title);
            embed.put("description", description);
            embed.put("color", color);
            if (fields != null && !fields.isEmpty()) {
                embed.put("fields", fields);
            }

            Map<String, Object> body = new HashMap<>();
            body.put("embeds", List.of(embed));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            restTemplate.postForEntity(webhookUrl, new HttpEntity<>(body, headers), String.class);
            log.info("Discord notification sent: {}", title);
        } catch (Exception e) {
            log.error("Failed to send Discord notification: {}", e.getMessage());
        }
    }
}
