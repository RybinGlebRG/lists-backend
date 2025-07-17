package ru.rerumu.lists.domain.factories.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.domain.factories.UserServiceProxyFactory;
import ru.rerumu.lists.dao.user.UserMapper;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.services.user.UserService;

@Deprecated
@Component
public class UserServiceProxyFactoryImpl implements UserServiceProxyFactory {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
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
//        Long authUserId = (Long)RequestContextHolder.currentRequestAttributes().getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
//        logger.info(String.format("GOT USER %d",authUserId));
    }

    @Override
    public UserService getUserServiceProtectionProxy(Long authUserId) throws EntityNotFoundException {
//        if (authUserId == null){
//            throw new IllegalArgumentException();
//        }
//        CrudRepositoryEntityImpl<User,Long> usersRepositoryImpl = new CrudRepositoryEntityImpl<>(userMapper);
//        UserServiceImpl userService = new UserServiceImpl(
//                usersRepository,
//                usersRepositoryImpl,
//                jwtSecret
//        );
//        Optional<User> authUser = userService.getOne(authUserId);
//        UserServiceProtectionProxyImpl protectionProxy = new UserServiceProtectionProxyImpl(
//                userService,
//                authUser.orElseThrow(EntityNotFoundException::new),
//                usersRepository,
//                usersRepositoryImpl,
//                jwtSecret
//        );
//        return protectionProxy;
        return null;
    }
}
