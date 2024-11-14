package com.craftilio.notification_service.interfaces;

import com.craftilio.notification_service.model.Notification;

public interface NotificationStrategy {
    void sendNotification(Notification notification);
}
