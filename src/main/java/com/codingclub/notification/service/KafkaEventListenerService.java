package com.codingclub.notification.service;

import com.codingclub.common.event.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaEventListenerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaEventListenerService.class);

    @Autowired
    private NotificationHandlerService notificationHandlerService;

    @KafkaListener(topics = "user-created-topic", groupId = "notification-group")
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        log.info("Received UserCreatedEvent for User ID {}", event.getUserId());
        notificationHandlerService.notifyNewUserSignup(
                event.getUserId(), event.getUsername(), event.getEmail());
    }

    @KafkaListener(topics = "user-approved-topic", groupId = "notification-group")
    public void handleUserApprovedEvent(UserApprovedEvent event) {
        log.info("Received UserApprovedEvent for User ID {}", event.getUserId());
        notificationHandlerService.notifyUserApproved(event.getUserId(), event.getUsername(), event.getEmail());
    }

    @KafkaListener(topics = "user-rejected-topic", groupId = "notification-group")
    public void handleUserRejectedEvent(UserRejectedEvent event) {
        log.info("Received UserRejectedEvent for User ID {}", event.getUserId());
        notificationHandlerService.notifyUserRejected(
                event.getUserId(), event.getUsername(), event.getEmail(), event.getReason());
    }

    @KafkaListener(topics = "project-submitted-topic", groupId = "notification-group")
    public void handleProjectSubmittedEvent(ProjectSubmittedEvent event) {
        log.info("Received ProjectSubmittedEvent for Project ID {}", event.getProjectId());
        notificationHandlerService.notifyNewProjectSubmission(
                event.getProjectId(), event.getTitle(), event.getDescription(),
                event.getCategory(), event.getSubmitterName(), event.getSubmitterEmail());
    }

    @KafkaListener(topics = "project-approved-topic", groupId = "notification-group")
    public void handleProjectApprovedEvent(ProjectApprovedEvent event) {
        log.info("Received ProjectApprovedEvent for Project ID {}", event.getProjectId());
        notificationHandlerService.notifyProjectApproved(
                event.getProjectId(), event.getTitle(), event.getSubmitterName(), event.getSubmitterEmail());
    }

    @KafkaListener(topics = "project-rejected-topic", groupId = "notification-group")
    public void handleProjectRejectedEvent(ProjectRejectedEvent event) {
        log.info("Received ProjectRejectedEvent for Project ID {}", event.getProjectId());
        notificationHandlerService.notifyProjectRejected(
                event.getProjectId(), event.getTitle(), event.getSubmitterName(),
                event.getSubmitterEmail(), event.getReason());
    }

    @KafkaListener(topics = "event-created-topic", groupId = "notification-group")
    public void handleEventCreatedEvent(EventCreatedEvent event) {
        log.info("Received EventCreatedEvent for Event ID {}", event.getEventId());
        notificationHandlerService.notifyNewCommunityEvent(event.getEventId(), event.getTitle());
    }
}
