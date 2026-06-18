package com.codingclub.notification.service;

import com.codingclub.notification.model.PushSubscription;
import com.codingclub.notification.repository.PushSubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PushNotificationService {

    private static final Logger log = LoggerFactory.getLogger(PushNotificationService.class);

    @Autowired
    private PushSubscriptionRepository subscriptionRepository;

    @Transactional
    public void subscribe(Long userId, String endpoint, String p256dh, String auth) {
        Optional<PushSubscription> existing = subscriptionRepository.findByEndpoint(endpoint);
        
        if (existing.isPresent()) {
            PushSubscription sub = existing.get();
            sub.setUserId(userId);
            sub.setP256dh(p256dh);
            sub.setAuth(auth);
            subscriptionRepository.save(sub);
        } else {
            PushSubscription sub = new PushSubscription();
            sub.setUserId(userId);
            sub.setEndpoint(endpoint);
            sub.setP256dh(p256dh);
            sub.setAuth(auth);
            subscriptionRepository.save(sub);
        }
    }

    @Transactional
    public void unsubscribe(String endpoint) {
        subscriptionRepository.deleteByEndpoint(endpoint);
    }

    public void sendPushNotification(Long userId, String title, String body, String url) {
        // Placeholder for actual Web Push API integration (e.g., using nl.martijndwars:web-push)
        log.info("Sending push notification to User {}: Title={}, Body={}, URL={}", userId, title, body, url);
    }

    public void sendPushNotificationToAll(String title, String body, String url) {
        // Placeholder for actual Web Push API integration
        log.info("Sending push notification to ALL subscribers: Title={}, Body={}, URL={}", title, body, url);
    }
}
