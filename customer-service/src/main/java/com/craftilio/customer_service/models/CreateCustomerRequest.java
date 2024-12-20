package com.craftilio.customer_service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String idNumber;
    private String gender;
    private String county;
    private String physicalAddress;
    private String password;

}
