package com.craftilio.account_service.controllers;

import com.craftilio.account_service.models.CreateAccountRequest;
import com.craftilio.account_service.models.UpdateAccountRequest;
import com.craftilio.account_service.services.AccountService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/create")
    private ResponseEntity<?> createCustomer(@RequestBody @Valid CreateAccountRequest request){
        return accountService.createAccount(request);
    }

    @GetMapping("/get/{accountNumber}")
    private ResponseEntity<?> get(@PathVariable String accountNumber){
        return accountService.getAccount(accountNumber);
    }

    @PutMapping("/close/{accountNumber}")
    private ResponseEntity<?> close(@PathVariable String accountNumber){
        return accountService.closeAccount(accountNumber);
    }

    @Hidden
    @PostMapping("/update")
    private ResponseEntity<?> update(@RequestBody @Valid UpdateAccountRequest request){
        return accountService.updateAccountBalance(request);
    }

}
