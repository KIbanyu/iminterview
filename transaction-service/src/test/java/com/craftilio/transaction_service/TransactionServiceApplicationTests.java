package com.craftilio.transaction_service;

import com.craftilio.transaction_service.dtos.TransactionEntity;
import com.craftilio.transaction_service.integration.AccountService;
import com.craftilio.transaction_service.models.Account;
import com.craftilio.transaction_service.models.AccountDetails;
import com.craftilio.transaction_service.models.TransactionRequest;
import com.craftilio.transaction_service.models.UpdateAccountRequest;
import com.craftilio.transaction_service.repos.TransactionsRepo;
import com.craftilio.transaction_service.services.iml.DefaultTransactionService;
import com.craftilio.transaction_service.services.iml.KafkaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


class TransactionServiceApplicationTests {

	@Mock
	private TransactionsRepo transactionsRepo;

	@Mock
	private KafkaService kafkaService;

	@Mock
	private AccountService accountService;

	@InjectMocks
	private DefaultTransactionService transactionService;

	private Account account;
	private TransactionRequest transactionRequest;
	private UpdateAccountRequest updateAccountRequest;

	@BeforeEach
	void setUp() {
		account = Account.builder().accountNumber("1234").balance(BigDecimal.valueOf(1000)).status("ACTIVE").build();

		transactionRequest = new TransactionRequest();
		transactionRequest.setAccount("12345");
		transactionRequest.setAmount(BigDecimal.valueOf(500));
		transactionRequest.setNarration("Test transaction");
		updateAccountRequest = new UpdateAccountRequest();
		updateAccountRequest.setAccountUpdates(List.of(account));

		MockitoAnnotations.openMocks(this);
		transactionService = new DefaultTransactionService(transactionsRepo, accountService,kafkaService);
		transactionService = new DefaultTransactionService(transactionsRepo, accountService,kafkaService);

	}

	@Test
	void testDepositCallsUpdateAccountSuccess() {
		AccountDetails accountDetails = AccountDetails.builder().account(account).status(200).build();
		accountDetails.setAccount(account);
		accountDetails.setStatus(200);
		when(accountService.getAccountDetails(eq("12345"))).thenReturn(accountDetails);
		when(accountService.updateAccount(any(UpdateAccountRequest.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.OK));
		ResponseEntity<?> response = transactionService.deposit(transactionRequest);
		verify(transactionsRepo, times(1)).save(any(TransactionEntity.class));
		verify(accountService, times(1)).updateAccount(any(UpdateAccountRequest.class));
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	void testWithdrawCallsUpdateAccountSuccess() {
		AccountDetails accountDetails = AccountDetails.builder().account(account).status(200).build();
		accountDetails.setAccount(account);
		accountDetails.setStatus(200);
		when(accountService.getAccountDetails(eq("12345"))).thenReturn(accountDetails);
		when(accountService.updateAccount(any(UpdateAccountRequest.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.OK));
		ResponseEntity<?> response = transactionService.withdraw(transactionRequest);
		verify(transactionsRepo, times(1)).save(any(TransactionEntity.class));
		verify(accountService, times(1)).updateAccount(any(UpdateAccountRequest.class));
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}


	@Test
	void testWithdrawInsufficientBalance() {
		transactionRequest.setAmount(BigDecimal.valueOf(1500));  // Higher than balance
		AccountDetails accountDetails = AccountDetails.builder().account(account).status(200).build();
		accountDetails.setAccount(account);
		accountDetails.setStatus(200);
		when(accountService.getAccountDetails(eq("12345"))).thenReturn(accountDetails);
		ResponseEntity<?> response = transactionService.withdraw(transactionRequest);
		verify(accountService, never()).updateAccount(any(UpdateAccountRequest.class));
		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}



	@Test
	void testTransferInsufficientBalance() {
		transactionRequest.setAmount(BigDecimal.valueOf(1500));
		AccountDetails fromAccountDetails = AccountDetails.builder().status(200).account(account).status(200).build();
		AccountDetails toAccountDetails = AccountDetails.builder().status(400).account(Account.builder().build()).status(200).build();
		when(accountService.getAccountDetails(eq("12345"))).thenReturn(fromAccountDetails);
		when(accountService.getAccountDetails(eq("67890"))).thenReturn(toAccountDetails);
		ResponseEntity<?> response = transactionService.transfer(transactionRequest);
		verify(accountService, never()).updateAccount(any(UpdateAccountRequest.class));
		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
	}


}
