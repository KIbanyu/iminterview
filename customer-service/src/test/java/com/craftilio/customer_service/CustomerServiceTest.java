package com.craftilio.customer_service;

import com.craftilio.customer_service.controllers.CustomerController;
import com.craftilio.customer_service.models.CreateCustomerRequest;
import com.craftilio.customer_service.models.CustomerDetails;
import com.craftilio.customer_service.models.LoginRequest;
import com.craftilio.customer_service.models.UpdateCustomerDetailsRequest;
import com.craftilio.customer_service.repos.CustomerRepo;
import com.craftilio.customer_service.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerServiceTest {
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void testRegisterSuccess() throws Exception {
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .idNumber("1234")
                .gender("M")
                .county("County")
                .physicalAddress("123 Street")
                .password("password")
                .build();

        when(customerService.register(any(CreateCustomerRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        mockMvc.perform(post("/api/v1/customers/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }


    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("john@example.com", "password");

        when(customerService.login(any(LoginRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(post("/api/v1/customers/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print());
    }


    @Test
    void testUpdateSuccess() throws Exception {

        UpdateCustomerDetailsRequest request = UpdateCustomerDetailsRequest.builder()
                .customerId(UUID.randomUUID())
                .county("County")
                .email("john@example.com")
                .phoneNumber("1234567890")
                .lastName("Doe")
                .password("password")
                .physicalAddress("new address")
                .build();
        when(customerService.update(any(UpdateCustomerDetailsRequest.class)))
                .thenReturn(new ResponseEntity<>(HttpStatus.OK));

        mockMvc.perform(put("/api/v1/customers/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void testGetCustomerDetailsSuccess() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDetails details = CustomerDetails.builder()
                .status(200)
                .message("Customer found")
                .build();
        when(customerService.getCustomerDetails(customerId)).thenReturn(details);
        mockMvc.perform(get("/api/v1/customers/customer-details/{customerId}", customerId))
                .andExpect(status().isOk())
                .andDo(print());
    }




    @Test
    void testGetCustomerDetailsNotFound() throws Exception {
        UUID customerId = UUID.randomUUID();
        CustomerDetails details = CustomerDetails.builder()
                .status(404)
                .message("Customer not found")
                .data(null)
                .build();

        when(customerService.getCustomerDetails(customerId)).thenReturn(details);

        mockMvc.perform(get("/api/v1/customers/customer-details/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").doesNotExist())
                .andDo(print());
    }


}
