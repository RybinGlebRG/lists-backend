package ru.rerumu.lists.services.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.user.UserMapper;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.user.UserService;
import ru.rerumu.lists.services.user.impl.UserServiceImpl;
import ru.rerumu.lists.services.user.impl.UserServiceProtectionProxyImpl;

import java.util.Optional;

@Configuration
public class UserServiceConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean("UserService")
    public UserService getUserService(
            UsersRepository usersRepository,
            UserMapper userMapper,
            @Value("${jwt.secret}") byte[] jwtSecret
    ){
        CrudRepositoryEntityImpl<User,Long> usersRepositoryImpl = new CrudRepositoryEntityImpl<>(userMapper);
        UserServiceImpl userServiceImpl = new UserServiceImpl(
                usersRepository,
                usersRepositoryImpl,
                jwtSecret
        );
        return userServiceImpl;

    }

    @Bean("UserServiceProtectionProxy")
    @Primary
    @RequestScope
    public UserService getProtectionProxy(
            @Qualifier("UserService") UserService userService
    ) throws EntityNotFoundException {
        Long authUserId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
        Optional<User> authUser = userService.getOne(authUserId);
        logger.info(String.format("GOT USER %d",authUserId));
        UserServiceProtectionProxyImpl protectionProxy = new UserServiceProtectionProxyImpl(
                userService,
                authUser.orElseThrow()
        );
        return protectionProxy;

    }
}
