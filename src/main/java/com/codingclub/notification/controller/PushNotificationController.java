package com.codingclub.notification.controller;

import com.codingclub.notification.service.PushNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class PushNotificationController {

    @Autowired
    private PushNotificationService pushNotificationService;

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(
            @RequestHeader("X-User-Id") String userIdHeader,
            @RequestBody Map<String, Object> payload) {
        
        Long userId = Long.valueOf(userIdHeader);
        String endpoint = (String) payload.get("endpoint");
        
        @SuppressWarnings("unchecked")
        Map<String, String> keys = (Map<String, String>) payload.get("keys");
        
        if (endpoint == null || keys == null || !keys.containsKey("p256dh") || !keys.containsKey("auth")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid subscription data"));
        }

        pushNotificationService.subscribe(userId, endpoint, keys.get("p256dh"), keys.get("auth"));
        return ResponseEntity.ok(Map.of("message", "Subscribed successfully"));
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<Map<String, String>> unsubscribe(@RequestBody Map<String, String> payload) {
        String endpoint = payload.get("endpoint");
        if (endpoint == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Endpoint is required"));
        }
        
        pushNotificationService.unsubscribe(endpoint);
        return ResponseEntity.ok(Map.of("message", "Unsubscribed successfully"));
    }

    @PostMapping("/send")
    public ResponseEntity<Map<String, String>> sendNotification(@RequestBody Map<String, Object> payload) {
        String title = (String) payload.get("title");
        String body = (String) payload.get("body");
        String url = (String) payload.get("url");
        Object userIdObj = payload.get("userId");

        if (title == null || body == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Title and body are required"));
        }

        if (userIdObj != null) {
            Long userId = Long.valueOf(userIdObj.toString());
            pushNotificationService.sendPushNotification(userId, title, body, url);
            return ResponseEntity.ok(Map.of("message", "Notification sent to user " + userId));
        } else {
            pushNotificationService.sendPushNotificationToAll(title, body, url);
            return ResponseEntity.ok(Map.of("message", "Notification sent to all subscribers"));
        }
    }
}
