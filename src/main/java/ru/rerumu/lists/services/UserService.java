package ru.rerumu.lists.services;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.exception.EmptyMandatoryParameterException;
import ru.rerumu.lists.exception.IncorrectPasswordException;
import ru.rerumu.lists.model.Book;
import ru.rerumu.lists.model.TokenRequest;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.UsersRepository;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class UserService {

    @Autowired
    private UsersRepository usersRepository;

    @Value("${jwt.secret}")
    private byte[] jwtSecret;

    @Transactional(rollbackFor = Exception.class)
    public String createToken(TokenRequest tokenRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, IncorrectPasswordException {
        User user = usersRepository.getOne(tokenRequest.getUsername());
        boolean isValid = isValidPassword(tokenRequest.getPassword(),user.getHashedPassword());
        if (!isValid){
            throw new IncorrectPasswordException();
        }
        String jwt = createJWT(user.getName());
        return jwt;
    }

    private boolean isValidPassword(String requestPassword, String hashedPassword) throws NoSuchAlgorithmException, InvalidKeySpecException {
        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new BouncyCastleProvider());
        String[] parts = hashedPassword.split("\\$");
        int iterations = Integer.parseInt(parts[2]);
        byte[] salt = Base64.getDecoder().decode(parts[3]);
        byte[] hash = Base64.getDecoder().decode(parts[4]);
        // TODO: Specify charset
        KeySpec keySpec = new PBEKeySpec(requestPassword.toCharArray(), salt, iterations, 256);
        SecretKey secretKey2 = new SecretKeySpec(
                SecretKeyFactory
                        .getInstance("PBKDF2WithHmacSHA256")
                        .generateSecret(keySpec)
                        .getEncoded()
                , "AES");
        byte[] tmp = secretKey2.getEncoded();
//        String tt = Base64.getEncoder().encodeToString(tmp);
        if (Arrays.equals(hash,tmp)){
            return true;
        }

        return false;

    }

    private String createJWT(String username){
//        Key tmp = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//        String str = Base64.getEncoder().encodeToString(tmp.getEncoded());
        Key key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE,15);
        Date expiration = calendar.getTime();
        String jws = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date())
                .setId(UUID.randomUUID().toString())
                .setNotBefore(new Date())
                .setExpiration(expiration)
                .claim("identity",username)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return jws;
    }
}
