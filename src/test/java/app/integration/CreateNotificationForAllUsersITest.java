package app.integration;


import app.model.Notification;
import app.model.NotificationStatus;
import app.repository.NotificationRepository;
import app.service.NotificationService;
import app.web.dto.SendNotificationForAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CreateNotificationForAllUsersITest {


    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;
    private List<UUID> userIds;

    @BeforeEach
    void setUp() {
        userIds = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            userIds.add(UUID.randomUUID());
        }
    }

    @Test
    void sendNotificationToAllUsers_happyPath() {
        SendNotificationForAll sendNotificationForAll = new SendNotificationForAll();
        sendNotificationForAll.setSubject("Test Subject");
        sendNotificationForAll.setBody("Test Body");
        sendNotificationForAll.setStatus(NotificationStatus.SUCCEEDED);
        sendNotificationForAll.setUserId(userIds);

        List<Notification> notificationForAll = notificationService.createNotificationForAll(sendNotificationForAll);

        assertNotNull(notificationForAll);
        assertEquals(5, notificationForAll.size());
        for (Notification notification : notificationForAll) {
            assertNotNull(notification.getBody());
            assertNotNull(notification.getSubject());
            assertNotNull(notification.getStatus());
            assertNotNull(notification.getUserId());
        }

    }

}
