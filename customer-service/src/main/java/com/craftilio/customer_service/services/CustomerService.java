package com.craftilio.customer_service.services;

import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.models.LoginRequest;
import com.craftilio.customer_service.models.UpdateCustomerDetailsRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CustomerService {
    ResponseEntity<?> createCustomer(CreateCustomerRequest request);
    ResponseEntity<?> customerLogin(LoginRequest request);
    ResponseEntity<?> updateCustomerProfile(UpdateCustomerDetailsRequest request);
    ResponseEntity<?> getAllCustomers();

}
