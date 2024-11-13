package com.craftilio.transaction_service.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TransactionRequest {
    private BigDecimal amount;
    private String account;
    private String toAccount;
    public String narration;
}
