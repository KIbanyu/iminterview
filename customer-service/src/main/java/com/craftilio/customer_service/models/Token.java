package com.craftilio.customer_service.models;

import lombok.Data;

@Data
public class Token {

    private String email;
    private String accessToken;
    private String refreshToken;
}
