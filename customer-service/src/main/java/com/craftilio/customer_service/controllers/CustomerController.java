package com.craftilio.customer_service.controllers;


import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.models.LoginRequest;
import com.craftilio.customer_service.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/customer-login")
    private ResponseEntity<?> customerLogin(@RequestBody @Valid LoginRequest request){
        return customerService.customerLogin(request);
    }

    @PostMapping("/update-profile")
    private ResponseEntity<?> updateCustomerDetail(){
        return customerService.getAllCustomers();
    }



    @GetMapping("/get-all-customers")
    private ResponseEntity<?> getAllCustomers(){
        return customerService.getAllCustomers();
    }



}
