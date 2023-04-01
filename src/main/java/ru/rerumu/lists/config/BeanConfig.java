package ru.rerumu.lists.config;

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
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.BookTypeMapper;
import ru.rerumu.lists.mappers.GameMapper;
import ru.rerumu.lists.mappers.UserMapper;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.model.Game;
import ru.rerumu.lists.model.User;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.UsersRepository;
import ru.rerumu.lists.repository.impl.BookRepositoryImpl;
import ru.rerumu.lists.repository.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.*;

import java.util.Optional;

@Configuration
public class BeanConfig {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Bean
    public BookRepository getBookRepository(
            BookMapper bookMapper,
            BookTypeMapper bookTypeMapper
            ) {
        CrudRepositoryEntityImpl<BookType, Integer> bookTypeRepository1 = new CrudRepositoryEntityImpl<>(bookTypeMapper);
        return new BookRepositoryImpl(
                bookMapper,
                bookTypeRepository1
        );
    }

    @Bean
    public BookTypesService getBookTypesService(
            BookTypeMapper bookTypeMapper
    ) {
        CrudRepositoryEntityImpl<BookType, Integer> bookTypeRepository = new CrudRepositoryEntityImpl<>(bookTypeMapper);
        return new BookTypesService(bookTypeRepository);
    }

    @Bean
    public GameService getGameService(
            GameMapper gameMapper
    ) {
        CrudRepositoryEntityImpl<Game, Integer> gameRepository = new CrudRepositoryEntityImpl<>(gameMapper);
        return new GameService(gameRepository);
    }

//    @Bean
//    @Deprecated
//    public UserServiceImpl getUserServiceOld(
//            UsersRepository usersRepository,
//            UserMapper userMapper,
//            @Value("${jwt.secret}") byte[] jwtSecret
//    ){
//        CrudRepositoryEntityImpl<User,Long> usersRepositoryImpl = new CrudRepositoryEntityImpl<>(userMapper);
//        return new UserServiceImpl(
//                usersRepository,
//                usersRepositoryImpl,
//                jwtSecret
//        );
//    }

    @Bean("UserService")
    public UserService getUserService(
            UsersRepository usersRepository,
            UserMapper userMapper,
            @Value("${jwt.secret}") byte[] jwtSecret
    ){
//
        CrudRepositoryEntityImpl<User,Long> usersRepositoryImpl = new CrudRepositoryEntityImpl<>(userMapper);
        UserServiceImpl userServiceImpl = new UserServiceImpl(
                usersRepository,
                usersRepositoryImpl,
                jwtSecret
        );
        return userServiceImpl;
//        Optional<User> authUser = userServiceImpl.getOne(authUserId);
//        UserServiceProtectionProxyImpl protectionProxy = new UserServiceProtectionProxyImpl(
//                userServiceImpl,
//                authUser.orElseThrow(),
//                usersRepository,
//                usersRepositoryImpl,
//                jwtSecret
//        );
//        logger.info(String.format("GOT USER %d",authUserId));
//        return protectionProxy;
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
        logger.info(String.format("GOT USER %d",authUserId));
        return protectionProxy;

    }

//    @Bean
//    public UserServiceProxyFactory getFactory(
//            UserServiceImpl userServiceImpl,
//            UsersRepository usersRepository,
//            UserMapper userMapper,
//            @Value("${jwt.secret}") byte[] jwtSecret
//    ){
//        UserServiceProxyFactoryImpl proxyFactory = new UserServiceProxyFactoryImpl(
//                usersRepository,
//                userMapper,
//               jwtSecret
//        );
//        return proxyFactory;
//    }
}
