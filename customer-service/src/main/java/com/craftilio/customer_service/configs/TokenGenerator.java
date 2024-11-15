package com.craftilio.customer_service.configs;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import com.craftilio.customer_service.dtos.CustomerEntity;
import com.craftilio.customer_service.models.Token;
import lombok.AllArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TokenGenerator {
    private final JwtEncoder accessTokenEncoder;
  private final   JwtEncoder refreshTokenEncoder;

    private String createAccessToken(Authentication authentication) {
        CustomerEntity user = (CustomerEntity) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("customerService")
                .issuedAt(now)
                .expiresAt(now.plus(5, ChronoUnit.MINUTES))
                .subject(user.getEmail())
                .build();
        return accessTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }

    private String createRefreshToken(Authentication authentication) {
        CustomerEntity user = (CustomerEntity) authentication.getPrincipal();
        Instant now = Instant.now();

        JwtClaimsSet claimsSet = JwtClaimsSet.builder()
                .issuer("customerService")
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.DAYS))
                .subject(user.getEmail())
                .build();

        return refreshTokenEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    }
    public Token createToken(Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof CustomerEntity user)) {
            throw new BadCredentialsException(
                    MessageFormat.format("principal {0} is not of User type", authentication.getPrincipal().getClass())
            );
        }

        Token tokenDTO = new Token();
        tokenDTO.setEmail(user.getEmail());
        tokenDTO.setAccessToken(createAccessToken(authentication));

        String refreshToken;
        if (authentication.getCredentials() instanceof Jwt jwt) {
            Instant now = Instant.now();
            Instant expiresAt = jwt.getExpiresAt();
            Duration duration = Duration.between(now, expiresAt);
            long daysUntilExpired = duration.toDays();
            if (daysUntilExpired < 7) {
                refreshToken = createRefreshToken(authentication);
            } else {
                refreshToken = jwt.getTokenValue();
            }
        } else {
            refreshToken = createRefreshToken(authentication);
        }
        tokenDTO.setRefreshToken(refreshToken);

        return tokenDTO;
    }

}

