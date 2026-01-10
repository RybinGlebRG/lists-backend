package ru.rerumu.lists.services.user.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.crosscut.exception.IncorrectPasswordException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.controller.users.TokenRequest;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.user.UserService;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


@Component("UserService")
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final byte[] jwtSecret;
    private final DateFactory dateFactory;

    @Autowired
    public UserServiceImpl(
            UsersRepository usersRepository,
            @Value("${jwt.secret}") byte[] jwtSecret, DateFactory dateFactory
    ) {
        this.usersRepository = usersRepository;
        this.jwtSecret = jwtSecret;
        this.dateFactory = dateFactory;
    }

    @Transactional(rollbackFor = Exception.class)
    public String createToken(TokenRequest tokenRequest) throws IncorrectPasswordException {
        User user = usersRepository.getOne(tokenRequest.getUsername());
        boolean isValid = user.isValidPassword(tokenRequest.getPassword().toCharArray());
        if (isValid){
            return createJWT(user);
        } else {
            throw new IncorrectPasswordException();
        }
    }

    private String createJWT(User user) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));

        LocalDateTime issuedAt = dateFactory.getLocalDateTime();
        LocalDateTime expiration = issuedAt.plusMinutes(15L);

        return Jwts.builder()
                .header()
                .add("typ", "JWT")
                .and()
                .issuedAt(Date.from(issuedAt.toInstant(ZoneOffset.UTC)))
                .id(UUID.randomUUID().toString())
                .notBefore(Date.from(issuedAt.toInstant(ZoneOffset.UTC)))
                .expiration(Date.from(expiration.toInstant(ZoneOffset.UTC)))
                .claim("identity", user.getName())
                .signWith(key)
                .compact();
    }

    @Override
    public @NonNull User findByToken(@NonNull String token) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));

        Jws<Claims> claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
        String username = claims.getPayload().get("identity", String.class);
        return usersRepository.getOne(username);
    }

    @Override
    public @NonNull User create(Long id, @NonNull String name, char @NonNull [] plainPassword) {
        if (id == null) {
            id = usersRepository.getNextId();
        }
        return usersRepository.create(id, name, plainPassword);
    }

    // TODO: fix null
    @Override
    public User findById(@NonNull Long userId){
        return usersRepository.findById(userId);
    }

    public void checkOwnershipAuthor(String username, Long authorId)throws UserIsNotOwnerException {
        if (username == null || authorId == null){
            throw new IllegalArgumentException();
        }
        if (!usersRepository.isOwnerAuthor(username, authorId)){
            throw new UserIsNotOwnerException();
        }
    }
}
