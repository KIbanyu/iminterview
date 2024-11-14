package com.craftilio.account_service.models;

import lombok.Data;

import java.util.List;

@Data
public class UpdateAccountRequest {
    private List<AccountUpdate> accountUpdates;
}
