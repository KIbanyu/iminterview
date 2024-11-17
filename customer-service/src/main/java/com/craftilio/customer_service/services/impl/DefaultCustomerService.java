package com.craftilio.customer_service.services.impl;

import com.craftilio.customer_service.dtos.CustomerEntity;
import com.craftilio.customer_service.enums.EntityStatus;
import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.models.CustomerDetails;
import com.craftilio.customer_service.models.LoginRequest;
import com.craftilio.customer_service.models.UpdateCustomerDetailsRequest;
import com.craftilio.customer_service.repos.CustomerRepo;
import com.craftilio.customer_service.services.CustomerService;
import com.craftilio.customer_service.utils.ThreeDes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Slf4j
@AllArgsConstructor
public  class DefaultCustomerService implements CustomerService {
    private final CustomerRepo customerRepo;
    private HashMap<String, Object> response;
    private final ThreeDes threeDes;



    @Override
    public ResponseEntity<?> register(CreateCustomerRequest request) {
        log.info("Create customer request: {}", request);
        Map<String, Object> response = new HashMap<>();
        try {
            // Check if phone number is already registered
            if (customerRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent()) {
                response.put("status", HttpStatus.CONFLICT.value());
                response.put("message", "Phone number already in use");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            // Check if ID number is already registered
            if (customerRepo.findByIdNumber(request.getIdNumber()).isPresent()) {
                response.put("status", HttpStatus.CONFLICT.value());
                response.put("message", "ID number already in use");
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            CustomerEntity customerEntity = new CustomerEntity(
                    request.getFirstName(), request.getLastName(), request.getEmail(),
                    request.getGender(), request.getPhoneNumber(), request.getIdNumber(),
                    EntityStatus.ACTIVE, threeDes.encrypt(request.getPassword()), request.getPhysicalAddress(),
                    request.getCounty());
            customerEntity.setModifiedOn(new Date());
            customerRepo.save(customerEntity);

            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Customer created");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Error occurred while creating customer", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred while creating customer");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    public ResponseEntity<?> login(LoginRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<CustomerEntity> customerEntity = customerRepo.findByEmailAndPassword(request.getEmail(), threeDes.encrypt(request.getPassword()));
            if (customerEntity.isPresent()) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", "Successfully logged in");
                response.put("data", customerEntity.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            log.error("Error occurred during customer login", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred during customer login");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<?> update(UpdateCustomerDetailsRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
            CustomerEntity customerEntity = customerRepo.findById(request.getCustomerId()).orElse(null);

            if (customerEntity == null) {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "Customer not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // Update customer details
            customerEntity.setCounty(request.getCounty());
            customerEntity.setAddress(request.getPhysicalAddress());
            customerEntity.setPassword(request.getPassword());
            customerEntity.setEmail(request.getEmail());
            customerEntity.setPhoneNumber(request.getPhoneNumber());
            customerEntity.setLastName(request.getLastName());
            customerRepo.save(customerEntity);

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Customer updated successfully");
            response.put("data", customerEntity);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error occurred while updating customer details", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred while updating customer details");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public CustomerDetails getCustomerDetails(UUID customerId) {

        try {
            CustomerEntity customerEntity = customerRepo.findById(customerId).orElse(null);
            if (customerEntity == null) {
               return CustomerDetails.builder().status(404).message("Customer not found").build();
            }
            return CustomerDetails.builder().status(200).message("Customer found").
                    data(CustomerDetails.Customer.builder()
                            .firstName(customerEntity.getFirstName())
                            .lastName(customerEntity.getLastName())
                            .status(customerEntity.getStatus().name())
                            .build()).build();

        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting customer details", e);
        }
    }


}
