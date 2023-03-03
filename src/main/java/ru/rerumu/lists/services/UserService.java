package ru.rerumu.lists.services;

import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getOne(Long userId) throws EntityNotFoundException;
}
