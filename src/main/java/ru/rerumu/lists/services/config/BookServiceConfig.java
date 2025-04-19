package ru.rerumu.lists.services.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.user.UserFactory;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.services.book.impl.BookServiceProtectionProxy;
import ru.rerumu.lists.services.user.UserService;

@Configuration
@Slf4j
public class BookServiceConfig {


    public BookServiceProtectionProxy getBookServiceProtectionProxy(
            UserService userService,
            ReadListService readListService,
            UserFactory userFactory
    ) {
        Long authUserId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
        User authUser = userService.getOne(authUserId).orElseThrow(EntityNotFoundException::new);
        log .info(String.format("GOT USER %d", authUser.userId()));
        return new BookServiceProtectionProxy(readListService, authUser, userFactory);
    }
}
