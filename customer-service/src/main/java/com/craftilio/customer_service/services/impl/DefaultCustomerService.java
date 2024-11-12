package com.craftilio.customer_service.services.impl;

import com.craftilio.customer_service.dtos.CustomerEntity;
import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.repos.CustomerRepo;
import com.craftilio.customer_service.services.CustomerService;
import com.craftilio.customer_service.utils.AppUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
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
        try {
            //To create a customer, id number should be unique, phone number should be unique
            //Check if phone number is already registered
            CustomerEntity customerEntity = customerRepo.findByPhoneNumber(request.getPhoneNumber()).orElse(null);
            if (customerEntity != null) {
                response.put("status", HttpStatus.CONFLICT);
                response.put("message", "Phone number already in use");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            //Check if id number is already registered
             customerEntity = customerRepo.findByIdNumber(request.getIdNumber()).orElse(null);
            if (customerEntity != null) {
                response.put("status", HttpStatus.CONFLICT);
                response.put("message", "ID number already in use");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            customerEntity = new CustomerEntity(request.getFirstName(),request.getLastName(), request.getEmail(), request.getGender(), request.getPhoneNumber(),
                    request.getIdNumber(), "PENDING", AppUtil.generatePin(), request.getPhysicalAddress(), request.getCounty(), "","");
            customerEntity.setModifiedOn(new Date());
            customerRepo.save(customerEntity);
            response.put("status", HttpStatus.CREATED);
            response.put("message", "Customer created");
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        }catch (Exception e) {
            log.error("CREATING_CUSTOMER_EXCEPTION, msg= an error occurred while creating customer ", e);
            response.put("status", HttpStatus.INTERNAL_SERVER_ERROR);
            response.put("message", "Exception occurred while creating customer");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }


    }

    @Override
    public ResponseEntity<?> getAllCustomers() {
        return null;
    }

    @Override
    public ResponseEntity<?> getCustomerById(UUID customerId) {
        return null;
    }

    @Override
    public ResponseEntity<?> getCustomerByValue(String key, String value) {
        return null;
    }
}
