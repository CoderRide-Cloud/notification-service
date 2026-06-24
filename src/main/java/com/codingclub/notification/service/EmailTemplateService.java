package com.codingclub.notification.service;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    public String userApprovedHtml(String username) {
        return """
                <h2>Welcome to CoderRide!</h2>
                <p>Hi %s,</p>
                <p>Your account has been approved. You can now access the developer community platform.</p>
                """.formatted(username);
    }

    public String userRejectedHtml(String username, String reason) {
        return """
                <h2>Application Update</h2>
                <p>Hi %s,</p>
                <p>Your application was not approved at this time.</p>
                <p><strong>Reason:</strong> %s</p>
                """.formatted(username, reason);
    }

    public String newUserAdminHtml(Long userId, String username, String email) {
        return """
                <h2>New User Signup</h2>
                <p>User <strong>%s</strong> (ID: %d) signed up and needs approval.</p>
                <p>Email: %s</p>
                """.formatted(username, userId, email != null ? email : "N/A");
    }

    public String projectApprovedHtml(String title, String memberName) {
        return """
                <h2>Project Approved</h2>
                <p>Hi %s,</p>
                <p>Your project <strong>%s</strong> has been approved and is now live.</p>
                """.formatted(memberName, title);
    }

    public String projectRejectedHtml(String title, String memberName, String reason) {
        return """
                <h2>Project Review Update</h2>
                <p>Hi %s,</p>
                <p>Your project <strong>%s</strong> was not approved.</p>
                <p><strong>Reason:</strong> %s</p>
                """.formatted(memberName, title, reason != null ? reason : "No reason provided");
    }

    public String newProjectAdminHtml(Long projectId, String title, String submitterName) {
        return """
                <h2>New Project Submission</h2>
                <p>Project <strong>%s</strong> (ID: %d) was submitted by %s and needs review.</p>
                """.formatted(title, projectId, submitterName);
    }
}
