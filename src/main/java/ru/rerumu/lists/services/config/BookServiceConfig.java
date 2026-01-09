package ru.rerumu.lists.services.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.dao.user.UsersRepository;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.book.BookService;
import ru.rerumu.lists.services.book.impl.BookServiceProtectionProxy;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.user.UserService;

@Configuration
@Slf4j
public class BookServiceConfig {

    @Bean("BookServiceProtectionProxy")
    @Primary
    @RequestScope
    public BookService getBookServiceProtectionProxy(
            UserService userService,
            ReadListService readListService,
            UsersRepository usersRepository
    ) {
        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        User authUser = userService.getOne(authUserId);
        log .info(String.format("GOT USER %d", authUser.getId()));
        return new BookServiceProtectionProxy(readListService, authUser, usersRepository);
    }
}
