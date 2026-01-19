package ru.rerumu.lists.services.user.impl;

import com.jcabi.aspects.Loggable;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.rerumu.lists.controller.users.views.in.RefreshTokenView;
import ru.rerumu.lists.crosscut.exception.IncorrectPasswordException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.crosscut.utils.DateFactory;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.controller.users.TokenRequest;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.user.TokenPair;
import ru.rerumu.lists.services.user.UserService;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;


@Component("UserService")
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final byte[] jwtSecret;
    private final DateFactory dateFactory;
    private final Long jwtDuration;

    @Autowired
    public UserServiceImpl(
            UsersRepository usersRepository,
            @Value("${jwt.secret}") byte[] jwtSecret,
            DateFactory dateFactory,
            @Value("${jwt.duration}") Long jwtDuration
    ) {
        this.usersRepository = usersRepository;
        this.jwtSecret = jwtSecret;
        this.dateFactory = dateFactory;
        this.jwtDuration = jwtDuration;
    }

    @Transactional(rollbackFor = Exception.class)
    public TokenPair createToken(TokenRequest tokenRequest) throws IncorrectPasswordException {
        User user = usersRepository.getOne(tokenRequest.getUsername());
        boolean isValid = user.isValidPassword(tokenRequest.getPassword().toCharArray());
        if (isValid){

            // Create access and refresh tokens
            TokenPair tokenPair = new TokenPair(
                    createJWT(user, Duration.of(jwtDuration, ChronoUnit.MINUTES)),
                    createJWT(user, Duration.of(5, ChronoUnit.DAYS))
            );

            // Save refresh token id
            Jws<Claims> claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret)))
                    .build()
                    .parseSignedClaims(tokenPair.refreshToken());
            user.setRefreshTokenId(claims.getPayload().getId());

            usersRepository.save(user);

            // Create and return access token
            return tokenPair;
        } else {
            throw new IncorrectPasswordException();
        }
    }

    private String createJWT(User user, Duration duration) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));

        LocalDateTime issuedAt = dateFactory.getLocalDateTime();
        LocalDateTime expiration = issuedAt.plus(duration);

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
    @Loggable(value = Loggable.TRACE, prepend = true, trim = false)
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

    @Override
    public @NonNull TokenPair refreshToken(@NonNull RefreshTokenView refreshTokenView) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));

        Jws<Claims> refreshClaims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(refreshTokenView.getRefreshToken());
        String username = refreshClaims.getPayload().get("identity", String.class);
        User user = usersRepository.getOne(username);

        // Create access and refresh tokens
        TokenPair tokenPair = new TokenPair(
                createJWT(user, Duration.of(jwtDuration, ChronoUnit.MINUTES)),
                createJWT(user, Duration.of(5, ChronoUnit.DAYS))
        );

        // Save refresh token id
        Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret)))
                .build()
                .parseSignedClaims(tokenPair.refreshToken());
        user.setRefreshTokenId(claims.getPayload().getId());

        usersRepository.save(user);

        // Create and return access token
        return tokenPair;
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
