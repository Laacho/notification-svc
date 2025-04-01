package app.api;

import app.model.Notification;
import app.model.NotificationStatus;
import app.service.NotificationService;
import app.web.NotificationController;
import app.web.dto.CreateNotificationRequest;
import app.web.dto.SendNotificationForAll;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
public class NotificationControllerApiTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    private UUID userId;
    private Notification notification;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        notification = Notification.builder()
                .id(UUID.randomUUID())
                .subject("Test Subject")
                .body("Test Body")
                .userId(userId)
                .status(NotificationStatus.SUCCEEDED)
                .deleted(false)
                .createdOn(LocalDateTime.now())
                .build();
    }



    @Test
    void getHistory_ShouldReturnNotifications() throws Exception {

        List<Notification> notificationResponses = List.of(notification);

        when(notificationService.getNotificationHistory(any())).thenReturn(notificationResponses);

        MockHttpServletRequestBuilder request = get("/api/v1/notifications/history")
                .param("userId", userId.toString());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].body").isNotEmpty())
                .andExpect(jsonPath("$[0].subject").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty())
                .andExpect(jsonPath("$[0].createdOn").isNotEmpty());
    }


    @Test
    void createNotificationForUser_ShouldReturnCreatedNotification() throws Exception {
        CreateNotificationRequest createNotificationRequest =  CreateNotificationRequest.builder()
                .userId(userId)
                .body("Test Body")
                .subject("Test Subject")
                .build();
        when(notificationService.createNotificationForUser(createNotificationRequest)).thenReturn(notification);

        MockHttpServletRequestBuilder request = post("/api/v1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(createNotificationRequest));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("body").isNotEmpty())
                .andExpect(jsonPath("subject").isNotEmpty())
                .andExpect(jsonPath("status").isNotEmpty())
                .andExpect(jsonPath("createdOn").isNotEmpty());

        verify(notificationService, times(1)).createNotificationForUser(createNotificationRequest);
    }

    @Test
    void deleteNotification_ShouldReturnNoContent() throws Exception {
        doNothing().when(notificationService).deleteNotifications(userId);

        MockHttpServletRequestBuilder request = delete("/api/v1/notifications/delete")
                .param("userId", userId.toString());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).deleteNotifications(userId);
    }

    @Test
    void createNotificationForAll_ShouldReturnCreatedNotifications() throws Exception {
      SendNotificationForAll sendNotificationForAll = new  SendNotificationForAll();
        sendNotificationForAll.setSubject("Test Subject");
        sendNotificationForAll.setBody("Test Body");

        when(notificationService.createNotificationForAll(sendNotificationForAll)).thenReturn(List.of(notification));

        MockHttpServletRequestBuilder request = post("/api/v1/notifications/all")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sendNotificationForAll));
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].body").isNotEmpty())
                .andExpect(jsonPath("$[0].subject").isNotEmpty())
                .andExpect(jsonPath("$[0].status").isNotEmpty())
                .andExpect(jsonPath("$[0].createdOn").isNotEmpty());

        verify(notificationService, times(1)).createNotificationForAll(sendNotificationForAll);
    }
}
