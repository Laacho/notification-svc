package app.web.mapper;

import app.model.Notification;
import app.web.dto.NotificationResponse;
import lombok.experimental.UtilityClass;

@UtilityClass
public class DtoMapper {


    public static NotificationResponse toNotificationResponse(Notification notification) {

        return NotificationResponse.builder()
                .body(notification.getBody())
                .subject(notification.getSubject())
                .createdOn(notification.getCreatedOn())
                .status(notification.getStatus())
                .build();
    }
}
