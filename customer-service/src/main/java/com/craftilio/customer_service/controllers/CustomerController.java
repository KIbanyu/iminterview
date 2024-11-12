package com.craftilio.customer_service.controllers;


import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.services.CustomerService;
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

    @PostMapping("/create-customer")
    private ResponseEntity<?> createCustomer(@RequestBody @Valid CreateCustomerRequest request){
        return customerService.createCustomer(request);
    }

    @PostMapping("/get-all-customers")
    private ResponseEntity<?> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/get-customer/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable("customerId") String customerId) {
        return customerService.getCustomerById(UUID.fromString(customerId));
    }

    @GetMapping("/get-customer/{key}/{value}")
    public ResponseEntity<?> getCustomerByKey(@PathVariable("key") String key, @PathVariable("value") String value) {
        return customerService.getCustomerByValue(key, value);
    }


}
