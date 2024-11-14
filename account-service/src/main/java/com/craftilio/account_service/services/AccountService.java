package com.craftilio.account_service.services;

import com.craftilio.account_service.models.CreateAccountRequest;
import com.craftilio.account_service.models.UpdateAccountRequest;
import org.springframework.http.ResponseEntity;



public interface AccountService {
    ResponseEntity<?> createAccount(CreateAccountRequest createAccountRequest);
    ResponseEntity<?> closeAccount(String accountId);
    ResponseEntity<?> getAccount(String accountId);
    ResponseEntity<?> updateAccountBalance(UpdateAccountRequest request);
}
