package com.craftilio.account_service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {
    private String message;
    private String recipient;
    private String type;
}
