package ru.rerumu.lists.services.user.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.crosscut.exception.IncorrectPasswordException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.dao.base.CrudRepository;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.TokenRequest;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.user.UserService;

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
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final CrudRepository<User,Long> crudRepository;
    private final byte[] jwtSecret;

    public UserServiceImpl(UsersRepository usersRepository, CrudRepository<User, Long> crudRepository, byte[] jwtSecret) {
        this.usersRepository = usersRepository;
        this.crudRepository = crudRepository;
        this.jwtSecret = jwtSecret;
    }

    @Transactional(rollbackFor = Exception.class)
    public String createToken(TokenRequest tokenRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, IncorrectPasswordException {
        User user = usersRepository.getOne(tokenRequest.getUsername());
        boolean isValid = isValidPassword(tokenRequest.getPassword(), Arrays.toString(user.getHashedPassword()));
        if (!isValid){
            throw new IncorrectPasswordException();
        }
        String jwt = createJWT(user.getName());
        return jwt;
    }

    @Override
    public void add(User user) {
        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new BouncyCastleProvider());

        int iterations = 29000;
        byte[] salt = new byte[32];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(salt);

        String passwordString;

        try {
            KeySpec keySpec = new PBEKeySpec(user.getPassword().toCharArray(), salt, iterations, 256);
            SecretKey secretKey2 = new SecretKeySpec(
                    SecretKeyFactory
                            .getInstance("PBKDF2WithHmacSHA256")
                            .generateSecret(keySpec)
                            .getEncoded()
                    , "AES");
            byte[] tmp = secretKey2.getEncoded();
            passwordString=String.format(
                    "$pbkdf2-sha256$%d$%s$%s",
                    iterations,
                    new String(Base64.getEncoder().encode(salt), StandardCharsets.UTF_8),
                    new String(Base64.getEncoder().encode(tmp), StandardCharsets.UTF_8)
            );
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        usersRepository.create(user.getId(), user.getName(), passwordString.toCharArray());
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

    // TODO: fix null
    @Override
    public User getOne(@NonNull Long userId){
        return usersRepository.findById(userId);
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

    public String checkTokenAndGetIdentity(String token){
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(jwtSecret))
                .build()
                .parseClaimsJws(token);
        return claims.getBody().get("identity", String.class);
    }

    public User checkTokenAndGetUser(String token){
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(Base64.getDecoder().decode(jwtSecret))
                .build()
                .parseClaimsJws(token);
        String username = claims.getBody().get("identity", String.class);
        User user = usersRepository.getOne(username);
        return user;
    }

    public void checkOwnershipList(String username, Long listId)throws UserIsNotOwnerException {
        if (username == null || listId == null){
            throw new IllegalArgumentException();
        }
        if (!usersRepository.isOwner(username, listId)){
            throw new UserIsNotOwnerException();
        }
    }

    public void checkOwnershipAuthor(String username, Long authorId)throws UserIsNotOwnerException {
        if (username == null || authorId == null){
            throw new IllegalArgumentException();
        }
        if (!usersRepository.isOwnerAuthor(username, authorId)){
            throw new UserIsNotOwnerException();
        }
    }

    public void checkOwnershipBook(String username, Long bookId) throws UserIsNotOwnerException{
        if (username == null || bookId == null){
            throw new IllegalArgumentException();
        }
        if (!usersRepository.isOwnerBook(username, bookId)){
            throw new UserIsNotOwnerException();
        }
    }

    public void checkOwnershipSeries(String username, Long seriesId) throws UserIsNotOwnerException{
        if (username == null || seriesId == null){
            throw new IllegalArgumentException();
        }
        if (!usersRepository.isOwnerSeries(username, seriesId)){
            throw new UserIsNotOwnerException();
        }
    }

    public void checkOwnership(String username, BookAddView bookAddView) throws UserIsNotOwnerException{
        if (username == null || bookAddView == null){
            throw new IllegalArgumentException();
        }

        if (bookAddView.getAuthorId() != null) {
            checkOwnershipAuthor(username, bookAddView.getAuthorId());
        }
        if (bookAddView.getSeriesId() != null) {
            checkOwnershipSeries(username,bookAddView.getSeriesId());
        }
    }
}
