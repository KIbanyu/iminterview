package com.craftilio.transaction_service.integration;

import com.craftilio.transaction_service.configs.FeignClientConfigs;
import com.craftilio.transaction_service.models.Account;
import com.craftilio.transaction_service.models.AccountDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name = "account-service", configuration = FeignClientConfigs.class)
public interface AccountService {
    @GetMapping("get/{accountNumber}")
    AccountDetails getAccountDetails(@PathVariable("accountNumber") String accountNumber);

    @PostMapping("update")
    AccountDetails updateAccount(@RequestBody Account account);
}
