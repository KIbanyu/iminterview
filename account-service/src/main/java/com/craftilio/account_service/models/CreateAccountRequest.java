package com.craftilio.account_service.models;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAccountRequest {
    private UUID customerId;
    private String accountType;
    private String currencyCode;

}
