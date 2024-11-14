package com.craftilio.account_service;

import com.craftilio.account_service.dto.Accounts;
import com.craftilio.account_service.integrations.CustomerService;
import com.craftilio.account_service.models.CreateAccountRequest;
import com.craftilio.account_service.models.CustomerDetails;
import com.craftilio.account_service.repos.AccountRepo;
import com.craftilio.account_service.services.AccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional

class AccountServiceApplicationTests {

	@Autowired
	private  AccountService accountService;

	@Autowired
	private  AccountRepo accountRepository;
	@MockBean
	private CustomerService customerService;
	private UUID customerId;

	@BeforeEach
	public void setUp() {
		customerId = UUID.randomUUID();
		CustomerDetails.Customer customer = CustomerDetails.Customer.builder().firstName("John").lastName("Doe").status("ACTIVE").build();
		when(customerService.getCustomerDetails(customerId)).thenReturn(CustomerDetails.builder().status(200).message("Success").data(customer).build()); // Mock that customer exists
	}
	@Test
	public void testCreateAccount() {
		CreateAccountRequest accountRequest = new CreateAccountRequest();
		accountRequest.setCustomerId(customerId);
		accountRequest.setAccountType("SAVINGS");
		accountRequest.setCurrencyCode("KES");
		ResponseEntity<?> createdAccount = accountService.createAccount(accountRequest);
		assertNotNull(createdAccount);
		assertEquals(200, createdAccount.getStatusCode().value());
	}


	@Test
	public void testCreateAccountForNonExistingCustomer() {

		UUID nonExistingCustomerId = UUID.randomUUID();
		when(customerService.getCustomerDetails(nonExistingCustomerId)).thenReturn(CustomerDetails.builder().status(404).message("Customer not found").build());

		CreateAccountRequest accountRequest = new CreateAccountRequest();
		accountRequest.setCustomerId(nonExistingCustomerId);
		accountRequest.setAccountType("SAVINGS");
		accountRequest.setCurrencyCode("KES");
		ResponseEntity<?> createdAccount = accountService.createAccount(accountRequest);
		assertNotNull(createdAccount);
		assertEquals(404, createdAccount.getStatusCode().value());

	}

	@Test
	public void testGetAccountById() {
		Accounts account = new Accounts();
		account.setAccountNumber("1234567890");
		account.setAccountType("CHECKING");
		account.setBalance(BigDecimal.valueOf(500));
		account.setCustomerId(customerId);
		account.setStatus("ACTIVE");
		accountRepository.save(account);
		ResponseEntity<?> fetchedAccount = accountService.getAccount(account.getAccountNumber());
		assertNotNull(fetchedAccount);
		assertEquals(200, fetchedAccount.getStatusCode().value());
	}


	@Test
	public void testAccountNotFound() {
		ResponseEntity<?> fetchedAccount = accountService.getAccount("123456678");
		assertNotNull(fetchedAccount);
		assertEquals(404, fetchedAccount.getStatusCode().value());
	}

	@Test
	public void testCloseAccount() {
		Accounts account = new Accounts();
		account.setAccountNumber("1234567890");
		account.setAccountType("CHECKING");
		account.setBalance(BigDecimal.valueOf(500));
		account.setCustomerId(customerId);
		account.setStatus("ACTIVE");
		accountRepository.save(account);
		ResponseEntity<?> fetchedAccount = accountService.closeAccount(account.getAccountNumber());
		assertNotNull(fetchedAccount);
		assertEquals(200, fetchedAccount.getStatusCode().value());
	}

	@Test
	public void testCloseAccountForNonAccount() {
		ResponseEntity<?> fetchedAccount = accountService.closeAccount("123456678");
		assertNotNull(fetchedAccount);
		assertEquals(404, fetchedAccount.getStatusCode().value());
	}

}
