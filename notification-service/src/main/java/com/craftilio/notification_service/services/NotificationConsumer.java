package com.craftilio.notification_service.services;

import com.craftilio.notification_service.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "notification-topic", groupId = "notification-group")
    public void consumeNotification(String incomingMessage) {
        try {
            Notification notification = objectMapper.readValue(incomingMessage, Notification.class);
            notificationService.processNotification(notification);
        } catch (Exception e) {
            log.error("Error processing notification: {}", e.getMessage(), e);
        }
    }
}
