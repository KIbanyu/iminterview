package com.craftilio.transaction_service.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountDetails {
    private String message;
    private int status;
    private Account account;


}
