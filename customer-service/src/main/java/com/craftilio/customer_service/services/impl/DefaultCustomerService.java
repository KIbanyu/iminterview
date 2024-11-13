package com.craftilio.customer_service.services.impl;

import com.craftilio.customer_service.dtos.CustomerEntity;
import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.models.LoginRequest;
import com.craftilio.customer_service.models.UpdateCustomerDetailsRequest;
import com.craftilio.customer_service.repos.CustomerRepo;
import com.craftilio.customer_service.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;


@Service
@Slf4j
@AllArgsConstructor
public  class DefaultCustomerService implements CustomerService {
    private final CustomerRepo customerRepo;
    private HashMap<String, Object> response;
    @Override
    public ResponseEntity<?> createCustomer(CreateCustomerRequest request) {
        log.info("Create customer request: {}", request);
        response = new HashMap<>();
        try {
            //To create a customer, id number should be unique, phone number should be unique
            //Check if phone number is already registered
            CustomerEntity customerEntity = customerRepo.findByPhoneNumber(request.getPhoneNumber()).orElse(null);
            if (customerEntity != null) {
                response.put("status", HttpStatus.CONFLICT.value());
                response.put("message", "Phone number already in use");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            //Check if id number is already registered
             customerEntity = customerRepo.findByIdNumber(request.getIdNumber()).orElse(null);
            if (customerEntity != null) {
                response.put("status", HttpStatus.CONFLICT.value());
                response.put("message", "ID number already in use");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            //Check if id number is already registered
            customerEntity = customerRepo.findByIdNumber(request.getIdNumber()).orElse(null);
            if (customerEntity != null) {
                response.put("status", HttpStatus.CONFLICT.value());
                response.put("message", "ID number already in use");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            customerEntity = new CustomerEntity(request.getFirstName(),request.getLastName(), request.getEmail(), request.getGender(), request.getPhoneNumber(),
                    request.getIdNumber(), "PENDING", request.getPassword(), request.getPhysicalAddress(), request.getCounty(), "","");
            customerEntity.setModifiedOn(new Date());
            customerRepo.save(customerEntity);
            response.put("status", HttpStatus.CREATED.value());
            response.put("message", "Customer created");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("CREATING_CUSTOMER_EXCEPTION, msg= an error occurred while creating customer ", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred while creating customer");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }


    }

    @Override
    public ResponseEntity<?> customerLogin(LoginRequest request) {
        response = new HashMap<>();
        try {
            CustomerEntity customerEntity = customerRepo.findByEmailAndPassword(request.getEmail(), request.getPassword()).orElse(null);
            if (customerEntity != null) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", "Successfully logged in");
                response.put("data", customerEntity);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            response.put("status", HttpStatus.UNAUTHORIZED.value());
            response.put("message", "Invalid email or password");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }catch (Exception e) {
            log.error("LOGIN_CUSTOMERS_EXCEPTION, msg= an error occurred during customer login ", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred during customer login");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<?> updateCustomerProfile(UpdateCustomerDetailsRequest request) {
        response = new HashMap<>();
        try {
            CustomerEntity customerEntity = customerRepo.findById(request.getCustomerId()).orElse(null);
            if (customerEntity == null) {
                response.put("status", HttpStatus.NOT_FOUND.value());
                response.put("message", "Customer not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            customerEntity.setCounty(request.getCounty());
            customerEntity.setAddress(request.getPhysicalAddress());
            customerEntity.setPassword(request.getPassword());
            customerEntity.setEmail(request.getEmail());
            customerEntity.setPhoneNumber(request.getPhoneNumber());
            customerEntity.setLastName(request.getLastName());
            customerRepo.save(customerEntity);

            response.put("status", HttpStatus.OK.value());
            response.put("message", "Customer updated");
            response.put("data", customerEntity);
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            log.error("UPDATE_CUSTOMERS_EXCEPTION, msg= an error occurred while updating customer details", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred while updating customer details");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<?> getAllCustomers() {
        response = new HashMap<>();
        try {
            response.put("status", HttpStatus.OK);
            response.put("message", "Success");
            response.put("data", customerRepo.findAll());
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            log.error("GETTING_CUSTOMERS_EXCEPTION, msg= an error occurred while getting customers ", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred  while getting customers");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<?> getCustomerById(UUID id) {
        response = new HashMap<>();
        try {
            CustomerEntity customerEntity = customerRepo.findById(id).orElse(null);
            if (customerEntity != null) {
                response.put("status", HttpStatus.OK.value());
                response.put("message", "Success");
                response.put("data", customerEntity);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            response.put("status", HttpStatus.NOT_FOUND.value());
            response.put("message", "Customer not found");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            log.error("GETTING_CUSTOMER_EXCEPTION, msg= an error occurred while getting customer by id ", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred   while getting customer by id ");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


}
