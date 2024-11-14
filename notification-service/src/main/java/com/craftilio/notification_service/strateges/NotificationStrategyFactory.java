package com.craftilio.notification_service.strateges;

import com.craftilio.notification_service.interfaces.NotificationStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class NotificationStrategyFactory {
    private final Map<String, NotificationStrategy> strategies = new HashMap<>();
    public NotificationStrategyFactory() {
        strategies.put("SMS", new SmsNotificationStrategy());
        strategies.put("EMAIL", new EmailNotificationStrategy());
    }

    public NotificationStrategy getStrategy(String type) {
        return strategies.get(type.toUpperCase());
    }

}
