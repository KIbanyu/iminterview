package com.craftilio.account_service.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountUpdate {
    private BigDecimal balance;
    private String accountNumber;
}
