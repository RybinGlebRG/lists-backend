package ru.rerumu.lists.config.beans;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.mappers.UserMapper;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.UsersRepository;
import ru.rerumu.lists.repository.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.UserService;
import ru.rerumu.lists.services.UserServiceImpl;
import ru.rerumu.lists.services.UserServiceProtectionProxyImpl;

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
