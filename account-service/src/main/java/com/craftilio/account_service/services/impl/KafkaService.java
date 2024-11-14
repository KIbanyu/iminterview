package com.craftilio.account_service.services.impl;

import com.craftilio.account_service.models.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class KafkaService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Async
    public void sendToTransactionalTopic(Notification notification) {
        try {
            String notificationJson = objectMapper.writeValueAsString(notification);
            kafkaTemplate.send("notification-topic", notificationJson);
            log.info("Notification sent to topic: {}", notificationJson);
        } catch (Exception e) {
            log.error("Error sending notification to Kafka", e);
        }
    }

}

