package com.craftilio.customer_service.controllers;


import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.models.CustomerDetails;
import com.craftilio.customer_service.models.LoginRequest;
import com.craftilio.customer_service.models.UpdateCustomerDetailsRequest;
import com.craftilio.customer_service.services.CustomerService;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    private ResponseEntity<?> register(@RequestBody @Valid CreateCustomerRequest request){
        return customerService.register(request);
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody @Valid LoginRequest request){
        return customerService.login(request);
    }


    @PutMapping("/update")
    private ResponseEntity<?> update(@RequestBody @Valid UpdateCustomerDetailsRequest request){
        return customerService.update(request);
    }

    @Hidden
    @GetMapping("/customer-details/{customerId}")
    private CustomerDetails getCustomerDetails(@PathVariable("customerId") UUID customerId){
        return customerService.getCustomerDetails(customerId);
    }

}
