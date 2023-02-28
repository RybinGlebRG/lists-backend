package ru.rerumu.lists.services;

import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.CrudRepository;
import ru.rerumu.lists.repository.UsersRepository;

import java.util.Optional;

public class UserServiceProtectionProxyImpl implements UserService {

    private final UserServiceImpl userService;

    private final User authUser;

    @Deprecated
    private final UsersRepository usersRepository;
    private final CrudRepository<User,Long> crudRepository;
    private final byte[] jwtSecret;

    public UserServiceProtectionProxyImpl(UserServiceImpl userService, User authUser, UsersRepository usersRepository, CrudRepository<User, Long> crudRepository, byte[] jwtSecret) {
        this.userService = userService;
        this.authUser = authUser;
        this.usersRepository = usersRepository;
        this.crudRepository = crudRepository;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public Optional<User> getOne(Long userId) throws EntityNotFoundException {
        if (!authUser.userId().equals(userId)){
            throw new IllegalCallerException();
        }
        return userService.getOne(userId);
    }
}
