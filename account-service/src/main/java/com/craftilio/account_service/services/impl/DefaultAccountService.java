package com.craftilio.account_service.services.impl;

import com.craftilio.account_service.dto.Accounts;
import com.craftilio.account_service.integrations.CustomerService;
import com.craftilio.account_service.models.CreateAccountRequest;
import com.craftilio.account_service.models.CustomerDetails;
import com.craftilio.account_service.repos.AccountRepo;
import com.craftilio.account_service.services.AccountService;
import com.craftilio.account_service.utils.AppUtils;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static com.craftilio.account_service.utils.AppUtils.generateAccountNumber;
import static org.springframework.http.HttpStatus.*;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultAccountService implements AccountService {
    private final CustomerService customerService;
    private HashMap<String, Object> response;
    private final AccountRepo accountRepo;
    @Override
    public ResponseEntity<?> createAccount(CreateAccountRequest request) {
        response = new HashMap<>();
        try {
            CustomerDetails customerDetails = customerService.getCustomerDetails(request.getCustomerId());
            log.info("GETTING_CUSTOMER_DETAILS, msg = getting customer details response [{}]", customerDetails);
            if (customerDetails == null) {
                response.put("status", INTERNAL_SERVER_ERROR.value());
                response.put("message", INTERNAL_SERVER_ERROR.getReasonPhrase());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            if (customerDetails.getStatus() != 200){
                response.put("status",customerDetails.getStatus());
                response.put("message", customerDetails.getMessage());
                return new ResponseEntity<>(response, HttpStatus.valueOf(customerDetails.getStatus()));
            }

            Accounts accounts = new Accounts();
            accounts.setAccountNumber(generateAccountNumber());
            accounts.setAccountName(customerDetails.getData().getFirstName() + " " + customerDetails.getData().getLastName());
            accounts.setCurrency(request.getCurrencyCode());
            accounts.setAccountType(request.getAccountType());
            accounts.setBalance(BigDecimal.ZERO);
            accounts.setCustomerId(request.getCustomerId());
            accounts.setStatus("PENDING");
            accounts.setModifiedOn(new Date());
            accounts.setModifiedBy("Test User");
            accounts.setCreatedBy("Test User");
            accounts.setComment("New Account");
            accountRepo.save(accounts);
            response.put("status", HttpStatus.OK.value());
            response.put("message", "Account created");
            return new ResponseEntity<>(response, HttpStatus.OK);

        }catch (Exception e) {
            log.error("CREATING_ACCOUNT_EXCEPTION, msg= an error occurred while creating account ", e);
            response.put("status", INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred while occurred creating account ");
            return new ResponseEntity<>(response, INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<?> closeAccount(String accountNumber) {

        try {
            ResponseEntity<?> getAccount =getAccount(accountNumber);
            if (!getAccount.getStatusCode().is2xxSuccessful()){
                return getAccount;
            }
            response.clear();
            response = new HashMap<>();
            Accounts account = accountRepo.getByAccountNumber(accountNumber);

            if (account.getStatus().equalsIgnoreCase("CLOSED")) {
                response.put("status", OK.value());
                response.put("message", "Account is already closed");
                return new ResponseEntity<>(response, OK);
            }

            account.setStatus("CLOSED");
            accountRepo.save(account);
            response.put("status", OK.value());
            response.put("message", "Account closed successfully");
            return new ResponseEntity<>(response, OK);

        }catch (Exception e) {
            log.error("GETTING_ACCOUNT_EXCEPTION, msg= an error occurred while closing account ", e);
            response.put("status", INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred while occurred closing account ");
        return new ResponseEntity<>(response, OK);
        }

    }

    @Override
    public ResponseEntity<?> getAccount(String accountNumber) {
        response = new HashMap<>();
        try {
            Accounts account = accountRepo.findByAccountNumber(accountNumber).orElse(null);
            if (account == null) {
                response.put("status", NOT_FOUND.value());
                response.put("message", "Account not found");
                return new ResponseEntity<>(response, OK);
            }

            response.put("status", OK.value());
            response.put("message", "Account found");
            response.put("account", account);
            return new ResponseEntity<>(response, OK);


        }catch (Exception e) {
            log.error("GETTING_ACCOUNT_EXCEPTION, msg= an error while occurred getting account ", e);
            response.put("status", INTERNAL_SERVER_ERROR.value());
            response.put("message", "Exception occurred while occurred getting account ");
            return new ResponseEntity<>(response, OK);
        }

    }
}