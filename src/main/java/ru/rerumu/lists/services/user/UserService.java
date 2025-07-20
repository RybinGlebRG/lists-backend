package ru.rerumu.lists.services.user;

import ru.rerumu.lists.controller.book.view.in.BookAddView;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.IncorrectPasswordException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.domain.TokenRequest;
import ru.rerumu.lists.domain.user.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserService {
    User getOne(Long userId) throws EntityNotFoundException;

    @Deprecated
    void checkOwnershipList(String username, Long listId) throws UserIsNotOwnerException;

    @Deprecated
    void checkOwnershipAuthor(String username, Long authorId)throws UserIsNotOwnerException;

    @Deprecated
    void checkOwnershipBook(String username, Long bookId) throws UserIsNotOwnerException;

    @Deprecated
    void checkOwnershipSeries(String username, Long seriesId) throws UserIsNotOwnerException;

    @Deprecated
    void checkOwnership(String username, BookAddView bookAddView) throws UserIsNotOwnerException;

    @Deprecated
    String checkTokenAndGetIdentity(String token);

    @Deprecated
    User checkTokenAndGetUser(String token);

    @Deprecated
    String createToken(TokenRequest tokenRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, IncorrectPasswordException;

    void add(User user);


}
