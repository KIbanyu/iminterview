package com.craftilio.transaction_service.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public  class Account {
    private String status;
    private String type;
    private BigDecimal balance;
    private String accountNumber;
}