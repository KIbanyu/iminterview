package com.craftilio.customer_service.models;

import lombok.Data;

@Data
public class UpdateCustomerDetailsRequest {
    private String county;
    private String physicalAddress;
    private String password;
    private String phoneNumber;

}
