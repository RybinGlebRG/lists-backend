package ru.rerumu.lists.services.user.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.IncorrectPasswordException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.crosscut.exception.UserPermissionException;
import ru.rerumu.lists.domain.TokenRequest;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.user.UserService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class UserServiceProtectionProxyImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;

    private final User authUser;

    public UserServiceProtectionProxyImpl(
            UserService userService,
            User authUser
    ) {
        this.userService = userService;
        this.authUser = authUser;
    }

    @Override
    public User getOne(Long userId) throws EntityNotFoundException {
        if (!authUser.getId().equals(userId)){
            throw new UserPermissionException();
        }
        return userService.getOne(userId);
    }

    @Override
    public void checkOwnershipList(String username, Long listId) throws UserIsNotOwnerException {
        userService.checkOwnershipList(username,listId);
    }

    @Override
    public void checkOwnershipAuthor(String username, Long authorId) throws UserIsNotOwnerException {
        userService.checkOwnershipAuthor(username,authorId);
    }

    @Override
    public void checkOwnershipBook(String username, Long bookId) throws UserIsNotOwnerException {
        userService.checkOwnershipBook(username,bookId);
    }

    @Override
    public void checkOwnershipSeries(String username, Long seriesId) throws UserIsNotOwnerException {
        userService.checkOwnershipSeries(username, seriesId);
    }

    @Override
    public void checkOwnership(String username, BookAddView bookAddView) throws UserIsNotOwnerException {
        userService.checkOwnership(username, bookAddView);
    }

    @Override
    public String checkTokenAndGetIdentity(String token) {
        return userService.checkTokenAndGetIdentity(token);
    }

    @Override
    public User checkTokenAndGetUser(String token) {
        return userService.checkTokenAndGetUser(token);
    }

    @Override
    public String createToken(TokenRequest tokenRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, IncorrectPasswordException {
        return userService.createToken(tokenRequest);
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }
}
