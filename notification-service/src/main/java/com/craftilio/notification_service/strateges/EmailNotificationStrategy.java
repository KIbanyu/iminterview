package com.craftilio.notification_service.strateges;

import com.craftilio.notification_service.interfaces.NotificationStrategy;
import com.craftilio.notification_service.model.Notification;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailNotificationStrategy implements NotificationStrategy {
    @Override
    public void sendNotification(Notification notification) {
        log.info("Sending Email to {}: {}", notification.getRecipient(), notification.getMessage());

    }
}
