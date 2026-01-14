package ru.rerumu.lists.domain.user.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import ru.rerumu.lists.crosscut.exception.AppException;
import ru.rerumu.lists.domain.user.User;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

@ToString
public class UserImpl implements User {

    @Getter
    private final Long userId;

    @Getter
    private final String name;

    @Getter
    private final String password;

    @Getter
    @Setter
    private String refreshTokenId;


    public UserImpl(Long userId, String name, String password, String refreshTokenId) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.refreshTokenId = refreshTokenId;
    }

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public boolean isValidPassword(char[] password) {
        try {
            Security.setProperty("crypto.policy", "unlimited");
            Security.addProvider(new BouncyCastleProvider());
            String[] parts = this.password.split("\\$");
            int iterations = Integer.parseInt(parts[2]);
            byte[] salt = Base64.getDecoder().decode(parts[3]);
            byte[] hash = Base64.getDecoder().decode(parts[4]);
            // TODO: Specify charset
            KeySpec keySpec = new PBEKeySpec(password, salt, iterations, 256);
            SecretKey secretKey = new SecretKeySpec(
                    SecretKeyFactory
                            .getInstance("PBKDF2WithHmacSHA256")
                            .generateSecret(keySpec)
                            .getEncoded()
                    , "AES");
            byte[] tmp = secretKey.getEncoded();
            return Arrays.equals(hash, tmp);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AppException(e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userId);
    }

}
