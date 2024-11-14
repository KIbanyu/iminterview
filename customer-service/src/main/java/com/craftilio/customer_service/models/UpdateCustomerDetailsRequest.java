package com.craftilio.customer_service.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpdateCustomerDetailsRequest {
    private UUID customerId;
    private String county;
    private String physicalAddress;
    private String password;
    private String phoneNumber;
    private String email;
    private String lastName;

}
