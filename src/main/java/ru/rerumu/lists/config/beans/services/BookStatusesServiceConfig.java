package ru.rerumu.lists.config.beans.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.dao.book.status.BookStatusMapper;
import ru.rerumu.lists.model.BookStatusRecord;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.book.status.BookStatusesService;

@Configuration
public class BookStatusesServiceConfig {

    @Bean
    public BookStatusesService getBookStatusesService(
            BookStatusMapper bookStatusMapper
    ) {
        CrudRepositoryEntityImpl<BookStatusRecord, Integer> bookStatusesRepository = new CrudRepositoryEntityImpl<>(bookStatusMapper);
        return new BookStatusesService(bookStatusesRepository);
    }
}
