package app.service;

import app.model.Notification;
import app.repository.NotificationRepository;
import app.web.dto.CreateNotificationRequest;
import app.web.dto.SendNotificationForAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getNotificationHistory(UUID userId) {
        return notificationRepository.findAllByUserIdAndDeletedIsFalse(userId);
    }

    public Notification createNotificationForUser(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .status(request.getStatus())
                .userId(request.getUserId())
                .subject(request.getSubject())
                .body(request.getBody())
                .createdOn(LocalDateTime.now())
                .deleted(false)
                .build();
       return notificationRepository.save(notification);
    }

    public void deleteNotifications(UUID userId) {
        List<Notification> allByUserId = notificationRepository.findAllByUserId(userId);
        allByUserId.forEach(notification -> {
            notification.setDeleted(true);
            notificationRepository.save(notification);
        });
    }

    public List<Notification> createNotificationForAll(SendNotificationForAll sendNotificationForAll) {
        List<UUID> allUsers = sendNotificationForAll.getUserId();
        List<Notification> publishedNotifications=new ArrayList<>();
        for (UUID userId : allUsers) {
            CreateNotificationRequest request = CreateNotificationRequest.builder()
                    .body(sendNotificationForAll.getBody())
                    .subject(sendNotificationForAll.getSubject())
                    .userId(userId)
                    .status(sendNotificationForAll.getStatus())
                    .build();
            Notification notificationForUser = createNotificationForUser(request);
            publishedNotifications.add(notificationForUser);
        }
        notificationRepository.saveAll(publishedNotifications);
        return publishedNotifications;
    }
}
