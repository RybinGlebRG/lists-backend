package ru.rerumu.lists.domain.user.impl;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class UserFactoryImpl implements UserFactory {

    @Override
    public User build(Long userId, String name, char[] plainPassword) {

        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new BouncyCastleProvider());

        int iterations = 29000;
        byte[] salt = new byte[32];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        String hashedPassword;

        try {
            KeySpec keySpec = new PBEKeySpec(plainPassword, salt, iterations, 256);
            SecretKey secretKey2 = new SecretKeySpec(
                    SecretKeyFactory
                            .getInstance("PBKDF2WithHmacSHA256")
                            .generateSecret(keySpec)
                            .getEncoded()
                    , "AES");
            byte[] tmp = secretKey2.getEncoded();
            hashedPassword=String.format(
                    "$pbkdf2-sha256$%d$%s$%s",
                    iterations,
                    new String(Base64.getEncoder().encode(salt), StandardCharsets.UTF_8),
                    new String(Base64.getEncoder().encode(tmp), StandardCharsets.UTF_8)
            );
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return new UserImpl(
                userId,
                name,
                hashedPassword,
                null
        );
    }
}
