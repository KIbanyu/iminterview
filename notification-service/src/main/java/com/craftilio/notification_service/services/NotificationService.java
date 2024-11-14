package com.craftilio.notification_service.services;

import com.craftilio.notification_service.interfaces.NotificationStrategy;
import com.craftilio.notification_service.model.Notification;
import com.craftilio.notification_service.strateges.NotificationStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {
    private final NotificationStrategyFactory strategyFactory;
    public void processNotification(Notification notification) {
        NotificationStrategy strategy = strategyFactory.getStrategy(notification.getType());
        if (strategy != null) {
            strategy.sendNotification(notification);
        } else {
            throw new IllegalArgumentException("Unsupported notification type: " + notification.getType());
        }

    }
}
