package app.web.dto;

import app.model.NotificationStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateNotificationRequest {

    private String body;

    private String subject;

    private UUID userId;

    private NotificationStatus status;
}
