package com.codingclub.notification.service;

import com.codingclub.common.event.UserCreatedEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class KafkaEventListenerServiceTest {

    @Mock
    private NotificationHandlerService notificationHandlerService;

    @InjectMocks
    private KafkaEventListenerService listenerService;

    @Test
    void testHandleUserCreatedEvent() {
        UserCreatedEvent event = new UserCreatedEvent(1L, "12345", "krish", "krish@example.com", null, null);
        listenerService.handleUserCreatedEvent(event);
        verify(notificationHandlerService, times(1)).notifyNewUserSignup(1L, "krish", "krish@example.com");
    }
}
