package ru.rerumu.lists.factories.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.UserServiceProxyFactory;
import ru.rerumu.lists.mappers.UserMapper;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.UsersRepository;
import ru.rerumu.lists.repository.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.services.UserServiceImpl;
import ru.rerumu.lists.services.UserServiceProtectionProxyImpl;

import java.util.Optional;

@Component
public class UserServiceProxyFactoryImpl implements UserServiceProxyFactory {
    private final UsersRepository usersRepository;
    private final UserMapper userMapper;
    private final  byte[] jwtSecret;

    public UserServiceProxyFactoryImpl(
            UsersRepository usersRepository,
            UserMapper userMapper,
            @Value("${jwt.secret}") byte[] jwtSecret
    ) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
        this.jwtSecret = jwtSecret;
    }

    @Override
    public UserService getUserServiceProtectionProxy(Long authUserId) throws EntityNotFoundException {
        if (authUserId == null){
            throw new IllegalArgumentException();
        }
        CrudRepositoryEntityImpl<User,Long> usersRepositoryImpl = new CrudRepositoryEntityImpl<>(userMapper);
        UserServiceImpl userService = new UserServiceImpl(
                usersRepository,
                usersRepositoryImpl,
                jwtSecret
        );
        Optional<User> authUser = userService.getOne(authUserId);
        UserServiceProtectionProxyImpl protectionProxy = new UserServiceProtectionProxyImpl(
                userService,
                authUser.orElseThrow(EntityNotFoundException::new),
                usersRepository,
                usersRepositoryImpl,
                jwtSecret
        );
        return protectionProxy;
    }
}
