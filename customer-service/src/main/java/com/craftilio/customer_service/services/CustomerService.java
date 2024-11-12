package com.craftilio.customer_service.services;

import com.craftilio.customer_service.models.CreateCustomerRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CustomerService {
    ResponseEntity<?> createCustomer(CreateCustomerRequest request);
    ResponseEntity<?> getAllCustomers();
    ResponseEntity<?> getCustomerById(UUID customerId);

    ResponseEntity<?> getCustomerByValue(String key, String value);
}
