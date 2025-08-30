package ru.rerumu.lists.services.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestContextHolder;
import ru.rerumu.lists.crosscut.exception.EntityNotFoundException;
import ru.rerumu.lists.dao.book.readingrecord.ReadingRecordsRepository;
import ru.rerumu.lists.dao.book.readingrecord.mapper.ReadingRecordMapper;
import ru.rerumu.lists.domain.book.readingrecords.impl.ReadingRecordFactory;
import ru.rerumu.lists.domain.user.User;
import ru.rerumu.lists.services.AuthUserParser;
import ru.rerumu.lists.services.book.impl.ReadListService;
import ru.rerumu.lists.services.book.readingrecord.ReadingRecordService;
import ru.rerumu.lists.services.book.readingrecord.impl.ReadingRecordServiceImpl;
import ru.rerumu.lists.services.book.status.BookStatusesService;
import ru.rerumu.lists.services.protectionproxies.ReadingRecordProtectionProxy;
import ru.rerumu.lists.services.user.UserService;

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
        Long authUserId = AuthUserParser.getAuthUser(RequestContextHolder.currentRequestAttributes());
        User authUser = userService.getOne(authUserId);
        log.info("GOT USER {}", authUser.userId());
        return new ReadingRecordProtectionProxy(readingRecordService, authUser, readListService);
    }
}
