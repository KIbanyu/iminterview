package com.craftilio.transaction_service.models;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAccountRequest {
    private List<Account> accountUpdates;
}
