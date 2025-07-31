package ru.rerumu.lists.services.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.crosscut.utils.FuzzyMatchingService;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.domain.author.AuthorFactory;
import ru.rerumu.lists.domain.book.impl.BookFactoryImpl;
import ru.rerumu.lists.domain.series.SeriesFactory;
import ru.rerumu.lists.domain.tag.TagFactory;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.domain.user.UserFactory;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.book.BookService;
import ru.rerumu.lists.services.book.impl.BookServiceProtectionProxy;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.book.status.BookStatusesService;
import ru.rerumu.lists.services.book.type.BookTypesService;
import ru.rerumu.lists.services.user.UserService;

@Configuration
@Slf4j
public class BookServiceConfig {

    @Bean("BookServiceImpl")
    public BookService getTagService(
            BookRepository bookRepository,
            AuthorsBooksRepository authorsBooksRepository,
            BookTypesService bookTypesService,
            BookStatusesService bookStatusesService,
            FuzzyMatchingService fuzzyMatchingService,
            BookFactoryImpl bookFactory,
            TagFactory tagFactory,
            UserFactory userFactory,
            AuthorFactory authorFactory,
            SeriesFactory seriesFactory
    ) {
        return new ReadListService(
                bookRepository,
                authorsBooksRepository,
                bookTypesService,
                bookStatusesService,
                fuzzyMatchingService,
                bookFactory,
                tagFactory,
                userFactory,
                authorFactory,
                seriesFactory
        );
    }

    @Bean("BookServiceProtectionProxy")
    @Primary
    @RequestScope
    public BookService getBookServiceProtectionProxy(
            UserService userService,
            ReadListService readListService,
            UserFactory userFactory
    ) {
        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        User authUser = userService.getOne(authUserId);
        log .info(String.format("GOT USER %d", authUser.userId()));
        return new BookServiceProtectionProxy(readListService, authUser, userFactory);
    }
}
