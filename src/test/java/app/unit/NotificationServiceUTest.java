package app.unit;

import app.model.Notification;
import app.model.NotificationStatus;
import app.repository.NotificationRepository;
import app.service.NotificationService;
import app.web.dto.CreateNotificationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceUTest {

    @Mock
    private NotificationRepository notificationRepository;
    @InjectMocks
    private NotificationService notificationService;


    @Test
    void getNotificationHistory_ShouldReturnNotifications_WhenTheyExist() {
        // Given
        UUID userId = UUID.randomUUID();
        List<Notification> expectedNotifications = List.of(
                new Notification(UUID.randomUUID(), "Subject 1", "Body 1", userId, NotificationStatus.SUCCEEDED, false, LocalDateTime.now()),
                new Notification(UUID.randomUUID(), "Subject 2", "Body 2", userId, NotificationStatus.SUCCEEDED, false, LocalDateTime.now())
        );

        when(notificationRepository.findAllByUserIdAndDeletedFalse(userId)).thenReturn(expectedNotifications);

        // When
        List<Notification> actualNotifications = notificationService.getNotificationHistory(userId);

        // Then
        assertEquals(expectedNotifications, actualNotifications);
        verify(notificationRepository, times(1)).findAllByUserIdAndDeletedFalse(userId);
    }

    @Test
    void getNotificationHistory_ShouldReturnEmptyList_WhenNoNotificationsExist() {
        // Given
        UUID userId = UUID.randomUUID();
        when(notificationRepository.findAllByUserIdAndDeletedFalse(userId)).thenReturn(Collections.emptyList());

        // When
        List<Notification> actualNotifications = notificationService.getNotificationHistory(userId);

        // Then
        assertTrue(actualNotifications.isEmpty());
        verify(notificationRepository, times(1)).findAllByUserIdAndDeletedFalse(userId);
    }

    @Test
    void creatingNotificationForUser_happyPath() {
        CreateNotificationRequest request = CreateNotificationRequest.builder()
                .body("Body 1")
                .status(NotificationStatus.SUCCEEDED)
                .userId(UUID.randomUUID())
                .subject("Subject 1")
                .build();

        notificationService.createNotificationForUser(request);


        verify(notificationRepository, times(1)).save(any(Notification.class));

    }
}
