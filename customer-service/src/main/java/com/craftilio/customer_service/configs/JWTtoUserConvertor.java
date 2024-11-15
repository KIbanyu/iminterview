package com.craftilio.customer_service.configs;

import java.util.Collections;

import com.craftilio.customer_service.dtos.CustomerEntity;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;


@Component
public class JWTtoUserConvertor implements Converter<Jwt, UsernamePasswordAuthenticationToken> {

    @Override
    public UsernamePasswordAuthenticationToken convert(Jwt source) {
        CustomerEntity user = new CustomerEntity();
        user.setEmail(source.getSubject());
        return new UsernamePasswordAuthenticationToken(user, source, Collections.EMPTY_LIST);
    }


}