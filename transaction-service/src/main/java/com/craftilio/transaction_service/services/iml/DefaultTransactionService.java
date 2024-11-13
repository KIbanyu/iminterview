package com.craftilio.transaction_service.services.iml;

import com.craftilio.transaction_service.dtos.TransactionEntity;
import com.craftilio.transaction_service.enums.EntityStatus;
import com.craftilio.transaction_service.enums.TransactionType;
import com.craftilio.transaction_service.integration.AccountService;
import com.craftilio.transaction_service.models.Account;
import com.craftilio.transaction_service.models.AccountDetails;
import com.craftilio.transaction_service.models.TransactionRequest;
import com.craftilio.transaction_service.repos.TransactionsRepo;
import com.craftilio.transaction_service.services.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Service
@Slf4j
@AllArgsConstructor
public class DefaultTransactionService implements TransactionService {

    private final TransactionsRepo transactionsRepo;
    private final AccountService accountService;

    @Override
    public ResponseEntity<?> deposit(TransactionRequest request) {
        return processTransaction(request, TransactionType.DEPOSIT, "Deposit successful");
    }

    @Override
    public ResponseEntity<?> withdraw(TransactionRequest request) {
        return processTransaction(request, TransactionType.WITHDRAWAL, "Withdrawal successful");
    }

    @Override
    public ResponseEntity<?> transfer(TransactionRequest request) {
        return processTransfer(request);
    }

    private ResponseEntity<?> processTransaction(TransactionRequest request, TransactionType transactionType, String successMessage) {
        try {
            Account account = getAccountDetails(request.getAccount());
            if (account == null) {
                return createErrorResponse("Account not found");
            }

            if (transactionType == TransactionType.WITHDRAWAL && account.getBalance().compareTo(request.getAmount()) < 0) {
                return createErrorResponse("Insufficient balance");
            }

            processTransactionEntity(request, transactionType);
            updateAccountBalance(account, transactionType, request.getAmount());

            return createSuccessResponse(successMessage);

        } catch (Exception e) {
            log.error("Transaction exception", e);
            return createErrorResponse("Exception occurred during transaction");
        }
    }

    private ResponseEntity<?> processTransfer(TransactionRequest request) {
        try {
            Account fromAccount = getAccountDetails(request.getAccount());
            Account toAccount = getAccountDetails(request.getToAccount());

            if (fromAccount == null || toAccount == null) {
                return createErrorResponse("One or both accounts not found");
            }

            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                return createErrorResponse("Insufficient balance");
            }

            processTransactionEntity(request, TransactionType.TRANSFER);
            updateAccountBalance(fromAccount, TransactionType.TRANSFER, request.getAmount());
            updateAccountBalance(toAccount, TransactionType.TRANSFER, request.getAmount().negate());
            return createSuccessResponse("Transfer successful");

        } catch (Exception e) {
            log.error("Transfer exception", e);
            return createErrorResponse("Exception occurred during transfer");
        }
    }

    private Account getAccountDetails(String accountId) {

        if (getAccount(accountId).getStatusCode().equals(HttpStatus.OK)) {
            
        }




        return (Account) getAccount(accountId).getBody();
    }

    public ResponseEntity<?> getAccount(String accountId) {

        Map<String, Object> response = new HashMap<>();
        AccountDetails accountDetails = accountService.getAccountDetails(accountId);

        log.info("GETTING_ACCOUNT msg=response {}", accountDetails.getStatus());


        if (accountDetails.getStatus() != 200){
            response.put("status",accountDetails.getStatus());
            response.put("message", accountDetails.getMessage());
            return new ResponseEntity<>(response, HttpStatus.valueOf(accountDetails.getStatus()));
        }

        if (!accountDetails.getAccount().getStatus().equalsIgnoreCase(EntityStatus.ACTIVE.toString())) {
            response.put("status", BAD_REQUEST.value());
            response.put("message", "Invalid account status " + accountDetails.getAccount().getStatus() + " for account " + accountId);
            return new ResponseEntity<>(response, BAD_REQUEST);
        }

        return new ResponseEntity<>(accountDetails.getAccount(), OK);
    }

    private void processTransactionEntity(TransactionRequest request, TransactionType transactionType) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(request.getAmount());
        transaction.setStatus(EntityStatus.SUCCESSFUL);
        if (transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.TRANSFER) {
            transaction.setToAccountId(request.getAccount());
        } else {
            transaction.setFromAccountId(request.getAccount());
        }
        transactionsRepo.save(transaction);
    }

    private void updateAccountBalance(Account account, TransactionType transactionType, BigDecimal amount) {
        if (transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.TRANSFER) {
            account.setBalance(account.getBalance().add(amount));
        } else if (transactionType == TransactionType.WITHDRAWAL) {
            account.setBalance(account.getBalance().subtract(amount));
        }
        accountService.updateAccount(account);
    }

    private ResponseEntity<?> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("message", message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<?> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", OK.value());
        response.put("message", message);
        return new ResponseEntity<>(response, OK);
    }
}
