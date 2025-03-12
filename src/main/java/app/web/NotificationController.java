package app.web;

import app.model.Notification;
import app.service.NotificationService;
import app.web.dto.CreateNotificationRequest;
import app.web.dto.NotificationResponse;
import app.web.dto.SendNotificationForAll;
import app.web.mapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    //get(/history) za istoriqta na notifikaciite

    @GetMapping("/history")
    public ResponseEntity<List<NotificationResponse>> getHistory(@RequestParam(name = "userId") UUID userId) {
        List<NotificationResponse> list = notificationService.getNotificationHistory(userId)
                .stream()
                .map(DtoMapper::toNotificationResponse)
                .toList();


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(list);
    }
    //post(kogato admin prashta notificiq(shte ima proverka dali e za specifichen user ili za vsichki
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotificationForUser(@RequestBody CreateNotificationRequest request) {
        Notification notification=    notificationService.createNotificationForUser(request);

        NotificationResponse notificationResponse = DtoMapper.toNotificationResponse(notification);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(notificationResponse);
    }
    //delete kogato user si gi trie
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteNotification(@RequestParam(name = "userId") UUID userId) {
        notificationService.deleteNotifications(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/all")
    public ResponseEntity<List<NotificationResponse>> createNotificationForAll(@RequestBody SendNotificationForAll sendNotificationForAll) {
        List<NotificationResponse> list = notificationService.createNotificationForAll(sendNotificationForAll)
                .stream()
                .map(DtoMapper::toNotificationResponse)
                .toList();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(list);
    }
}
