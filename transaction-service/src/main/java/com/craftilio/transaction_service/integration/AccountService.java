package com.craftilio.transaction_service.integration;

import com.craftilio.transaction_service.configs.FeignClientConfigs;
import com.craftilio.transaction_service.models.AccountDetails;
import com.craftilio.transaction_service.models.UpdateAccountRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "account-service", configuration = FeignClientConfigs.class)
public interface AccountService {
    @GetMapping("get/{accountNumber}")
    AccountDetails getAccountDetails(@PathVariable("accountNumber") String accountNumber);

    @PostMapping("update")
    ResponseEntity<?> updateAccount(@RequestBody UpdateAccountRequest account);
}
