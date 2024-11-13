package com.craftilio.transaction_service.controllers;


import com.craftilio.transaction_service.models.TransactionRequest;
import com.craftilio.transaction_service.services.TransactionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    private ResponseEntity<?> deposit(@RequestBody @Valid TransactionRequest request){
        return transactionService.deposit(request);
    }

    @PostMapping("/withdraw")
    private ResponseEntity<?> withdraw(@RequestBody @Valid TransactionRequest request){
        return transactionService.withdraw(request);
    }

    @PostMapping("/transfer")
    private ResponseEntity<?> transfer(@RequestBody @Valid TransactionRequest request){
        return transactionService.transfer(request);
    }
}
