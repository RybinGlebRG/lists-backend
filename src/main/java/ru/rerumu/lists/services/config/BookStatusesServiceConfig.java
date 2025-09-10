package ru.rerumu.lists.services.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDictionaryImpl;
import ru.rerumu.lists.dao.book.readingrecord.status.mapper.BookStatusMapper;
import ru.rerumu.lists.domain.bookstatus.BookStatusRecord;
import ru.rerumu.lists.services.book.status.BookStatusesService;

@Configuration
public class BookStatusesServiceConfig {

    @Bean
    public BookStatusesService getBookStatusesService(
            BookStatusMapper bookStatusMapper
    ) {
        CrudRepositoryDictionaryImpl<BookStatusRecord, Integer> bookStatusesRepository = new CrudRepositoryDictionaryImpl<>(bookStatusMapper);
        return new BookStatusesService(bookStatusesRepository);
    }
}
