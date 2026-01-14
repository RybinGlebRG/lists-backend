package ru.rerumu.lists.services.user.impl;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.controller.users.views.in.RefreshTokenView;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.IncorrectPasswordException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.controller.users.TokenRequest;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.user.TokenPair;
import ru.rerumu.lists.services.user.UserService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service("UserServiceProtectionProxy")
@Primary
@RequestScope
@Slf4j
public class UserServiceProtectionProxyImpl implements UserService {

    private final UserService userService;
    private final User authUser;

    @Autowired
    public UserServiceProtectionProxyImpl(
            @Qualifier("UserService") UserService userService
    ) {
        this.userService = userService;
        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        authUser = userService.findById(authUserId);
        log.info(String.format("GOT USER %d",authUserId));
    }

    @Override
    public User findById(Long userId) throws EntityNotFoundException {
        if (!authUser.getId().equals(userId)){
            throw new UserPermissionException();
        }
        return userService.findById(userId);
    }

    @Override
    public void checkOwnershipAuthor(String username, Long authorId) throws UserIsNotOwnerException {
        userService.checkOwnershipAuthor(username,authorId);
    }

    @Override
    public TokenPair createToken(TokenRequest tokenRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, IncorrectPasswordException {
        return userService.createToken(tokenRequest);
    }

    @Override
    public @NonNull User findByToken(@NonNull String token) {
        return userService.findByToken(token);
    }

    @Override
    public @NonNull User create(Long id, @NonNull String name, char @NonNull [] plainPassword) {
        return userService.create(id, name, plainPassword);
    }

    @Override
    public @NonNull TokenPair refreshToken(@NonNull RefreshTokenView refreshTokenView) {
        return userService.refreshToken(refreshTokenView);
    }
}
