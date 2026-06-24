package com.codingclub.notification.controller;

import com.codingclub.common.security.AuthUserContext;
import com.codingclub.common.security.AuthorizationService;
import com.codingclub.common.security.Permission;
import com.codingclub.common.web.AuthContextResolver;
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

    @Autowired
    private AuthContextResolver authContextResolver;

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Permissions", required = false) String permissions,
            @RequestHeader(value = "X-User-Position", required = false) String position,
            @RequestHeader(value = "X-User-Is-Lead", required = false) String isLead,
            @RequestHeader(value = "X-User-Is-Active", required = false) String isActive,
            @RequestHeader(value = "X-User-Custom-Role-Id", required = false) String customRoleId,
            @RequestBody Map<String, Object> payload) {

        AuthUserContext authUser = authContextResolver.resolve(userId, role, permissions, position, isLead, isActive, customRoleId);
        authorizationService.requireActive(authUser);

        String endpoint = (String) payload.get("endpoint");
        @SuppressWarnings("unchecked")
        Map<String, String> keys = (Map<String, String>) payload.get("keys");

        if (endpoint == null || keys == null || !keys.containsKey("p256dh") || !keys.containsKey("auth")) {
            return ResponseEntity.badRequest().body(Map.of("message", "Invalid subscription data"));
        }

        pushNotificationService.subscribe(authUser.getUserId(), endpoint, keys.get("p256dh"), keys.get("auth"));
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
    public ResponseEntity<Map<String, String>> sendNotification(
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            @RequestHeader(value = "X-User-Role", required = false) String role,
            @RequestHeader(value = "X-User-Permissions", required = false) String permissions,
            @RequestHeader(value = "X-User-Position", required = false) String position,
            @RequestHeader(value = "X-User-Is-Lead", required = false) String isLead,
            @RequestHeader(value = "X-User-Is-Active", required = false) String isActive,
            @RequestHeader(value = "X-User-Custom-Role-Id", required = false) String customRoleId,
            @RequestBody Map<String, Object> payload) {

        AuthUserContext authUser = authContextResolver.resolve(userId, role, permissions, position, isLead, isActive, customRoleId);
        authorizationService.requirePermission(authUser, Permission.SEND_NOTIFICATIONS);

        String title = payload.get("title") != null
                ? (String) payload.get("title")
                : (String) payload.get("subject");
        String body = payload.get("body") != null
                ? (String) payload.get("body")
                : (String) payload.get("content");
        String url = (String) payload.get("url");
        Object userIdObj = payload.get("userId");

        if (title == null || body == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Title and body are required"));
        }

        if (userIdObj != null) {
            Long targetUserId = Long.valueOf(userIdObj.toString());
            pushNotificationService.sendPushNotification(targetUserId, title, body, url);
            return ResponseEntity.ok(Map.of("message", "Notification sent to user " + targetUserId));
        }

        pushNotificationService.sendPushNotificationToAll(title, body, url);
        return ResponseEntity.ok(Map.of("message", "Notification sent to all subscribers"));
    }
}
