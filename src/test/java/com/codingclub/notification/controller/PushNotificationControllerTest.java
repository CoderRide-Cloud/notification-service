package com.codingclub.notification.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PushNotificationControllerTest {

    @InjectMocks
    private PushNotificationController controller;

    @Test
    void testContextLoads() {
        assertNotNull(controller);
    }
}
