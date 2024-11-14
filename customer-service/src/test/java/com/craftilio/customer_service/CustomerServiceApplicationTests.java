package com.craftilio.customer_service;

import com.craftilio.customer_service.dtos.CustomerEntity;
import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.models.LoginRequest;
import com.craftilio.customer_service.models.UpdateCustomerDetailsRequest;
import com.craftilio.customer_service.repos.CustomerRepo;
import com.craftilio.customer_service.services.CustomerService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


class CustomerServiceTest {

	@Mock
	private CustomerRepo customerRepo;

	@Mock
	private CustomerService customerService;

	@BeforeEach
    public void setup() {
		MockitoAnnotations.initMocks(this);
	}


	@Test
	void shouldCreateCustomerSuccessfully() {
		CreateCustomerRequest request = CreateCustomerRequest.builder()
				.firstName("John")
				.lastName("Doe")
				.email("john@example.com")
				.phoneNumber("123456789")
				.idNumber("ID1234")
				.gender("Male")
				.county("County")
				.physicalAddress("Address")
				.password("password")
				.build();

		// Mocking findByIdNumber to return Optional.empty() when no customer is found.
		when(customerRepo.findByIdNumber(request.getPhoneNumber())).thenReturn(Optional.empty());
		when(customerRepo.findByIdNumber(request.getIdNumber())).thenReturn(Optional.empty());

		ResponseEntity<?> response = customerService.register(request);

		// Verify the response status and body
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertTrue(response.getBody().toString().contains("Customer created"));
	}


	@Test
	void shouldReturnConflictWhenPhoneNumberAlreadyInUse() {
		CreateCustomerRequest request = CreateCustomerRequest.builder()
				.phoneNumber("123456789")
				.idNumber("ID1234")
				.build();

		when(customerRepo.findByPhoneNumber(request.getPhoneNumber()).isPresent()).thenReturn(true);
		ResponseEntity<?> response = customerService.register(request);
		assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
		assertTrue(response.getBody().toString().contains("Phone number already in use"));
	}

	@Test
	void shouldLoginSuccessfullyWithValidCredentials() {
		LoginRequest loginRequest = new LoginRequest("john@example.com", "password");
		CustomerEntity customerEntity = new CustomerEntity();
		when(customerRepo.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword()))
				.thenReturn(Optional.of(customerEntity));
		ResponseEntity<?> response = customerService.login(loginRequest);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().toString().contains("Successfully logged in"));
	}

	@Test
	void shouldReturnUnauthorizedForInvalidLogin() {
		LoginRequest loginRequest = new LoginRequest("john@example.com", "wrongpassword");
		when(customerRepo.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword()))
				.thenReturn(Optional.empty());

		ResponseEntity<?> response = customerService.login(loginRequest);

		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertTrue(response.getBody().toString().contains("Invalid email or password"));
	}

	@Test
	void shouldUpdateCustomerSuccessfully() {
		UpdateCustomerDetailsRequest request = UpdateCustomerDetailsRequest.builder()
				.customerId(UUID.randomUUID())
				.county("Nairobi")
				.physicalAddress("1234 Elm St")
				.password("newpassword123")
				.phoneNumber("+254712345678")
				.email("newemail@example.com")
				.lastName("Doe")
				.build();
		CustomerEntity existingCustomer = new CustomerEntity();
		existingCustomer.setId(UUID.randomUUID());
		when(customerRepo.findById(request.getCustomerId())).thenReturn(Optional.of(existingCustomer));
		ResponseEntity<?> response = customerService.update(request);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertTrue(response.getBody().toString().contains("Customer updated successfully"));
	}

	@Test
	void shouldReturnNotFoundWhenCustomerDoesNotExist() {
		UpdateCustomerDetailsRequest request = UpdateCustomerDetailsRequest.builder()
				.customerId(UUID.randomUUID())
				.county("Non Existent")
				.physicalAddress("1234 Elm St")
				.password("newpassword123")
				.phoneNumber("+254712345678")
				.email("newemail@example.com")
				.lastName("Doe")
				.build();
		when(customerRepo.findById(request.getCustomerId())).thenReturn(Optional.empty());
		ResponseEntity<?> response = customerService.update(request);
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody().toString().contains("Customer not found"));
	}
}
