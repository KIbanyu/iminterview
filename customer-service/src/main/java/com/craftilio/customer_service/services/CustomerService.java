package com.craftilio.customer_service.services;

import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.models.CustomerDetails;
import com.craftilio.customer_service.models.LoginRequest;
import com.craftilio.customer_service.models.UpdateCustomerDetailsRequest;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CustomerService {
    ResponseEntity<?> register(CreateCustomerRequest request);
    ResponseEntity<?> login(LoginRequest request);
    ResponseEntity<?> update(UpdateCustomerDetailsRequest request);
    CustomerDetails getCustomerDetails(UUID customerId);


}
