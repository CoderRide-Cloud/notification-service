package com.codingclub.notification.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PushNotificationServiceTest {

    @InjectMocks
    private PushNotificationService service;

    @Test
    void testContextLoads() {
        assertNotNull(service);
    }
}
