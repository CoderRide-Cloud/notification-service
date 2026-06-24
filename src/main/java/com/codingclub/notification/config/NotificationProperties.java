package com.codingclub.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "coderride.notifications")
public class NotificationProperties {
    private Email email = new Email();
    private Discord discord = new Discord();
    private String appUrl = "http://localhost:3000";

    @Data
    public static class Email {
        private String from = "notifications@coderride.com";
        private String admin = "admin@coderride.com";
        private String resendApiKey = "";
    }

    @Data
    public static class Discord {
        private String newUsers = "";
        private String approvals = "";
        private String rejections = "";
        private String newProjects = "";
        private String projectApprovals = "";
        private String projectRejections = "";
    }
}
