package com.craftilio.customer_service.configs;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

@Component
@Slf4j
public class KeyUtils {
    @Autowired
    Environment environment;

//    @Value("${access-token.private}")
    private String accessTokenPrivateKeyPath;

//    @Value("${access-token.public}")
    private String accessTokenPublicKeyPath;

//    @Value("${refresh-token.private}")
    private String refreshTokenPrivateKeyPath;

//    @Value("${refresh-token.public}")
    private String refreshTokenPublicKeyPath;

    private KeyPair _accessTokenKeyPair;
    private KeyPair _refreshTokenKeyPair;

    private KeyPair getAccessTokenKeyPair() {
        if (Objects.isNull(_accessTokenKeyPair)) {
            _accessTokenKeyPair = getKeyPair(accessTokenPublicKeyPath, accessTokenPrivateKeyPath);
        }
        return _accessTokenKeyPair;
    }

    private KeyPair getRefreshTokenKeyPair() {
        if (Objects.isNull(_refreshTokenKeyPair)) {
            _refreshTokenKeyPair = getKeyPair(refreshTokenPublicKeyPath, refreshTokenPrivateKeyPath);
        }
        return _refreshTokenKeyPair;
    }

    private KeyPair getKeyPair(String publicKeyPath, String privateKeyPath) {
        KeyPair keyPair;

        File publicKeyFile = new File(publicKeyPath);
        File privateKeyFile = new File(privateKeyPath);

        if (publicKeyFile.exists() && privateKeyFile.exists()) {
            //log.info("loading keys from file: {}, {}", publicKeyPath, privateKeyPath);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");

                byte[] publicKeyBytes = Files.readAllBytes(publicKeyFile.toPath());
                EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
                PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

                byte[] privateKeyBytes = Files.readAllBytes(privateKeyFile.toPath());
                PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
                PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

                keyPair = new KeyPair(publicKey, privateKey);
                return keyPair;
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (Arrays.stream(environment.getActiveProfiles()).anyMatch(s -> s.equals("prod"))) {
                throw new RuntimeException("public and private keys don't exist");
            }
        }

        File directory = new File("access-refresh-token-keys");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try {
            //log.info("Generating new public and private keys: {}, {}", publicKeyPath, privateKeyPath);
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
            try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyPair.getPublic().getEncoded());
                fos.write(keySpec.getEncoded());
            }

            try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyPair.getPrivate().getEncoded());
                fos.write(keySpec.getEncoded());
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }

        return keyPair;
    }


    public RSAPublicKey getAccessTokenPublicKey() {
        return (RSAPublicKey) getAccessTokenKeyPair().getPublic();
    };
    public RSAPrivateKey getAccessTokenPrivateKey() {
        return (RSAPrivateKey) getAccessTokenKeyPair().getPrivate();
    };
    public RSAPublicKey getRefreshTokenPublicKey() {
        return (RSAPublicKey) getRefreshTokenKeyPair().getPublic();
    };
    public RSAPrivateKey getRefreshTokenPrivateKey() {
        return (RSAPrivateKey) getRefreshTokenKeyPair().getPrivate();
    };



    public static PublicKey getPublicKeyFromString(String key) throws Exception {
        String publicKeyPEM = key.replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
    }

    public static PrivateKey getPrivateKeyFromString(String key) throws Exception {
        String privateKeyPEM = key.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded));
    }
}

