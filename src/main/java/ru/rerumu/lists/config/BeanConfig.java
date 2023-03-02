package ru.rerumu.lists.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.factories.UserServiceProxyFactory;
import ru.rerumu.lists.factories.impl.UserServiceProxyFactoryImpl;
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
import ru.rerumu.lists.services.BookTypesService;
import ru.rerumu.lists.services.GameService;
import ru.rerumu.lists.services.UserServiceImpl;

@Configuration
public class BeanConfig {

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

    @Bean
    @Deprecated
    public UserServiceImpl getUserService(
            UsersRepository usersRepository,
            UserMapper userMapper,
            @Value("${jwt.secret}") byte[] jwtSecret
    ){
        CrudRepositoryEntityImpl<User,Long> usersRepositoryImpl = new CrudRepositoryEntityImpl<>(userMapper);
        return new UserServiceImpl(
                usersRepository,
                usersRepositoryImpl,
                jwtSecret
        );
    }

    @Bean
    public UserServiceProxyFactory getFactory(
            UserServiceImpl userServiceImpl,
            UsersRepository usersRepository,
            UserMapper userMapper,
            @Value("${jwt.secret}") byte[] jwtSecret
    ){
        UserServiceProxyFactoryImpl proxyFactory = new UserServiceProxyFactoryImpl(
                usersRepository,
                userMapper,
               jwtSecret
        );
        return proxyFactory;
    }
}
