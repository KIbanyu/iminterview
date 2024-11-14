package com.craftilio.transaction_service.services.iml;

import com.craftilio.transaction_service.dtos.TransactionEntity;
import com.craftilio.transaction_service.enums.EntityStatus;
import com.craftilio.transaction_service.enums.TransactionType;
import com.craftilio.transaction_service.integration.AccountService;
import com.craftilio.transaction_service.models.Account;
import com.craftilio.transaction_service.models.AccountDetails;
import com.craftilio.transaction_service.models.TransactionRequest;
import com.craftilio.transaction_service.models.UpdateAccountRequest;
import com.craftilio.transaction_service.repos.TransactionsRepo;
import com.craftilio.transaction_service.services.TransactionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static org.springframework.http.HttpStatus.*;

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
            ResponseEntity<?> getAccountResponse = getAccount(getAccountDetails(request.getAccount()));

            if (getAccountResponse.getStatusCode() != OK) {
                return getAccountResponse;
            }
            Account account = getAccountDetails(request.getAccount()).getAccount();
            if (transactionType == TransactionType.WITHDRAWAL && account.getBalance().compareTo(request.getAmount()) < 0) {
                return createErrorResponse("Insufficient balance", BAD_REQUEST);
            }

            UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
            List<Account> accounts = new ArrayList<>();
            accounts.add(updateAccountBalance(account, transactionType, request.getAmount()));
            updateAccountRequest.setAccountUpdates(accounts);
            ResponseEntity<?> updateAccount =  accountService.updateAccount(updateAccountRequest);
            if (updateAccount.getStatusCode() != OK) {
                return createErrorResponse("Failed to update account balance", HttpStatus.valueOf(updateAccount.getStatusCode().value()));
            }
            processTransactionEntity(request, transactionType);
            return createSuccessResponse(successMessage);

        } catch (Exception e) {
            log.error("Transaction exception", e);
            return createErrorResponse("Exception occurred during transaction", INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> processTransfer(TransactionRequest request) {
        try {

            ResponseEntity<?> getAccountResponse = getAccount(getAccountDetails(request.getAccount()));

            if (getAccountResponse.getStatusCode() != OK) {
                return getAccountResponse;
            }
            Account fromAccount = getAccountDetails(request.getAccount()).getAccount();

            getAccountResponse = getAccount(getAccountDetails(request.getToAccount()));

            if (getAccountResponse.getStatusCode() != OK) {
                return getAccountResponse;
            }

            Account toAccount = getAccountDetails(request.getAccount()).getAccount();

            if (fromAccount == null || toAccount == null) {
                return createErrorResponse("One or both accounts not found", NOT_FOUND);
            }

            if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
                return createErrorResponse("Insufficient balance", BAD_REQUEST);
            }


            UpdateAccountRequest updateAccountRequest = new UpdateAccountRequest();
            List<Account> accounts = new ArrayList<>();
            accounts.add(updateAccountBalance(fromAccount, TransactionType.TRANSFER, request.getAmount()));
            accounts.add(updateAccountBalance(toAccount, TransactionType.TRANSFER, request.getAmount().negate()));
            updateAccountRequest.setAccountUpdates(accounts);
            ResponseEntity<?> updateAccount =  accountService.updateAccount(updateAccountRequest);
            if (updateAccount.getStatusCode() != OK) {
                return createErrorResponse("Failed to update account balance", HttpStatus.valueOf(updateAccount.getStatusCode().value()));
            }

            processTransactionEntity(request, TransactionType.TRANSFER);
            return createSuccessResponse("Transfer successful");

        } catch (Exception e) {
            log.error("Transfer exception", e);
            return createErrorResponse("Exception occurred during transfer", INTERNAL_SERVER_ERROR);
        }
    }

    private AccountDetails getAccountDetails(String accountId) {
      return accountService.getAccountDetails(accountId);

    }

    public ResponseEntity<?> getAccount(AccountDetails accountDetails) {

        Map<String, Object> response = new HashMap<>();
        if (accountDetails.getStatus() != 200){
            response.put("status",accountDetails.getStatus());
            response.put("message", accountDetails.getMessage());
            return new ResponseEntity<>(response, HttpStatus.valueOf(accountDetails.getStatus()));
        }

        if (!accountDetails.getAccount().getStatus().equalsIgnoreCase(EntityStatus.ACTIVE.toString())) {
            response.put("status", BAD_REQUEST.value());
            response.put("message", "Invalid account status " + accountDetails.getAccount().getStatus());
            return new ResponseEntity<>(response, BAD_REQUEST);
        }

        return new ResponseEntity<>(accountDetails, OK);
    }

    private void processTransactionEntity(TransactionRequest request, TransactionType transactionType) {
        TransactionEntity transaction = new TransactionEntity();
        transaction.setTransactionType(transactionType);
        transaction.setAmount(request.getAmount());
        transaction.setNarration(request.getNarration());
        transaction.setStatus(EntityStatus.SUCCESSFUL);
        transaction.setModifiedOn(new Date());

        if (transactionType == TransactionType.TRANSFER) {
            transaction.setFromAccountId(request.getAccount());
            transaction.setToAccountId(request.getToAccount());
        }

        if (transactionType == TransactionType.WITHDRAWAL) {
            transaction.setFromAccountId(request.getAccount());
        }

        if (transactionType == TransactionType.DEPOSIT) {
            transaction.setToAccountId(request.getAccount());
        }

        transactionsRepo.save(transaction);
    }

    private Account updateAccountBalance(Account account, TransactionType transactionType, BigDecimal amount) {
        if (transactionType == TransactionType.DEPOSIT || transactionType == TransactionType.TRANSFER) {
            account.setBalance(account.getBalance().add(amount));
        } else if (transactionType == TransactionType.WITHDRAWAL) {
            account.setBalance(account.getBalance().subtract(amount));
        }
        return account;
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<?> createSuccessResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", OK.value());
        response.put("message", message);
        return new ResponseEntity<>(response, OK);
    }
}
