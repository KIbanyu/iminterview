package com.craftilio.account_service.integrations;

import com.craftilio.account_service.configs.FeignClientConfigs;
import com.craftilio.account_service.models.CustomerDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "customer-service", configuration = FeignClientConfigs.class)
public interface CustomerService {
    @GetMapping("customer-details/{customerId}")
    CustomerDetails getCustomerDetails(@PathVariable("customerId") UUID customerId);
}
