package com.craftilio.transaction_service.services;

import com.craftilio.transaction_service.models.TransactionRequest;
import org.springframework.http.ResponseEntity;

public interface TransactionService {
    ResponseEntity<?> deposit(TransactionRequest request);
    ResponseEntity<?> withdraw(TransactionRequest request);
    ResponseEntity<?> transfer(TransactionRequest request);
}
