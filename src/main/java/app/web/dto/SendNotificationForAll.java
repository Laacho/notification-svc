package app.web.dto;

import app.model.NotificationStatus;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SendNotificationForAll {
    private String body;

    private String subject;

    private List<UUID> userId;

    private NotificationStatus status;
}
