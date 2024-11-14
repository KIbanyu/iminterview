package com.craftilio.customer_service.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDetails {
    private int status;
    private String message;
    private Customer data;

    @Data
    @Builder
    public static class Customer {
        private String firstName;
        private String lastName;
        private String status;
    }

}
