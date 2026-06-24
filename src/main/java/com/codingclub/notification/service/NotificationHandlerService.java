package com.codingclub.notification.service;

import com.codingclub.notification.config.NotificationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NotificationHandlerService {

    private static final Logger log = LoggerFactory.getLogger(NotificationHandlerService.class);
    private static final int COLOR_BLUE = 3447003;
    private static final int COLOR_GREEN = 3066993;
    private static final int COLOR_RED = 15158332;
    private static final int COLOR_PURPLE = 10181046;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DiscordService discordService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private NotificationProperties properties;

    public void notifyNewUserSignup(Long userId, String username, String email) {
        log.info("Handling new user signup notification for user {}", userId);

        discordService.sendEmbed(
                properties.getDiscord().getNewUsers(),
                "New User Signup",
                "A new user has registered and is awaiting approval",
                COLOR_BLUE,
                List.of(
                        field("Username", username, true),
                        field("Email", email != null ? email : "N/A", true),
                        field("User ID", String.valueOf(userId), true)
                )
        );

        emailService.sendEmail(
                properties.getEmail().getAdmin(),
                "New User Signup - Action Required",
                emailTemplateService.newUserAdminHtml(userId, username, email)
        );

        if (userId != null) {
            pushNotificationService.sendPushNotification(
                    userId,
                    "Welcome to CoderRide!",
                    "Hello " + username + ", complete your profile to start connecting.",
                    "/profile/edit"
            );
        }
    }

    public void notifyUserApproved(Long userId, String username, String email) {
        discordService.sendEmbed(
                properties.getDiscord().getApprovals(),
                "User Approved",
                username + " has been approved",
                COLOR_GREEN,
                List.of(field("Username", username, true), field("Email", email != null ? email : "N/A", true))
        );

        if (email != null && !email.isBlank()) {
            emailService.sendEmail(
                    email,
                    "Your Account Has Been Approved!",
                    emailTemplateService.userApprovedHtml(username)
            );
        }
    }

    public void notifyUserRejected(Long userId, String username, String email, String reason) {
        discordService.sendEmbed(
                properties.getDiscord().getRejections(),
                "User Rejected",
                username + "'s application has been rejected",
                COLOR_RED,
                List.of(field("Username", username, true), field("Reason", reason, false))
        );

        if (email != null && !email.isBlank()) {
            emailService.sendEmail(
                    email,
                    "Update on Your Application",
                    emailTemplateService.userRejectedHtml(username, reason)
            );
        }
    }

    public void notifyNewProjectSubmission(Long projectId, String title, String description, String category,
                                           String submitterName, String submitterEmail) {
        discordService.sendEmbed(
                properties.getDiscord().getNewProjects(),
                "New Project Submitted",
                "A new project is awaiting approval",
                COLOR_PURPLE,
                List.of(
                        field("Title", title, false),
                        field("Submitted by", submitterName, true),
                        field("Category", category != null ? category : "None", true)
                )
        );

        emailService.sendEmail(
                properties.getEmail().getAdmin(),
                "New Project Submission - Review Required",
                emailTemplateService.newProjectAdminHtml(projectId, title, submitterName)
        );
    }

    public void notifyProjectApproved(Long projectId, String title, String submitterName, String submitterEmail) {
        discordService.sendEmbed(
                properties.getDiscord().getProjectApprovals(),
                "Project Approved",
                "\"" + title + "\" has been approved",
                COLOR_GREEN,
                List.of(field("Project", title, true), field("Author", submitterName, true))
        );

        if (submitterEmail != null && !submitterEmail.isBlank()) {
            emailService.sendEmail(
                    submitterEmail,
                    "Your Project Has Been Approved!",
                    emailTemplateService.projectApprovedHtml(title, submitterName)
            );
        }
    }

    public void notifyProjectRejected(Long projectId, String title, String submitterName, String submitterEmail, String reason) {
        discordService.sendEmbed(
                properties.getDiscord().getProjectRejections(),
                "Project Rejected",
                "\"" + title + "\" was rejected",
                COLOR_RED,
                List.of(field("Project", title, true), field("Author", submitterName, true), field("Reason", reason, false))
        );

        if (submitterEmail != null && !submitterEmail.isBlank()) {
            emailService.sendEmail(
                    submitterEmail,
                    "Project Review Update",
                    emailTemplateService.projectRejectedHtml(title, submitterName, reason)
            );
        }
    }

    public void notifyNewCommunityEvent(Long eventId, String title) {
        pushNotificationService.sendPushNotificationToAll(
                "New Community Event!",
                title + " has just been scheduled.",
                "/events/" + eventId
        );
    }

    private Map<String, Object> field(String name, String value, boolean inline) {
        return Map.of("name", name, "value", value, "inline", inline);
    }
}
