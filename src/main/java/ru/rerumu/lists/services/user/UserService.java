package ru.rerumu.lists.services.user;

import lombok.NonNull;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.crosscut.exception.IncorrectPasswordException;
import ru.rerumu.lists.crosscut.exception.UserIsNotOwnerException;
import ru.rerumu.lists.domain.TokenRequest;
import ru.rerumu.lists.domain.user.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserService {
    User findById(Long userId) throws EntityNotFoundException;

    @Deprecated
    void checkOwnershipAuthor(String username, Long authorId)throws UserIsNotOwnerException;

    @Deprecated
    String createToken(TokenRequest tokenRequest) throws NoSuchAlgorithmException, InvalidKeySpecException, IncorrectPasswordException;

    @NonNull
    User findByToken(@NonNull String token);



    @NonNull
    User create(
            Long id,
            @NonNull String name,
            char @NonNull [] plainPassword
    );
}
