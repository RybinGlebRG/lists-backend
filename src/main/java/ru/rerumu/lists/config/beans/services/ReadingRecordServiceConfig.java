package ru.rerumu.lists.config.beans.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.mappers.ReadingRecordMapper;
import ru.rerumu.lists.model.user.User;
import ru.rerumu.lists.model.book.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.dao.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.services.book.ReadListService;
import ru.rerumu.lists.services.book.readingrecord.ReadingRecordService;
import ru.rerumu.lists.services.book.readingrecord.impl.ReadingRecordServiceImpl;
import ru.rerumu.lists.services.book.status.BookStatusesService;
import ru.rerumu.lists.services.protectionproxies.ReadingRecordProtectionProxy;
import ru.rerumu.lists.services.user.UserService;

import java.util.Optional;

@Configuration
@Slf4j
public class ReadingRecordServiceConfig {

    @Bean("ReadingRecordService")
    public ReadingRecordService getReadingRecordService(
            ReadingRecordMapper readingRecordMapper,
            BookStatusesService bookStatusesService,
            ReadingRecordsRepository readingRecordsRepository,
            ReadingRecordFactory readingRecordFactory
    ){
        return new ReadingRecordServiceImpl(
                readingRecordsRepository,
                bookStatusesService,
                readingRecordFactory
        );
    }

    @Bean("ReadingRecordProtectionProxy")
    @Primary
    @RequestScope
    public ReadingRecordService getReadingRecordProtectionProxy(
            @Qualifier("ReadingRecordService") ReadingRecordService readingRecordService,
            UserService userService,
            ReadListService readListService
    ) throws EntityNotFoundException {
        Long authUserId = (Long) RequestContextHolder.currentRequestAttributes().getAttribute("authUserId", RequestAttributes.SCOPE_REQUEST);
        Optional<User> authUser = userService.getOne(authUserId);
        log.info("GOT USER {}", authUser.orElseThrow().userId());
        return new ReadingRecordProtectionProxy(readingRecordService, authUser.orElseThrow(), readListService);
    }
}
