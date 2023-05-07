package ru.rerumu.lists.config.beans.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.mappers.BookTypeMapper;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.repository.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.BookTypesService;

@Configuration
public class BookTypesServiceConfig {

    @Bean
    public BookTypesService getBookTypesService(
            BookTypeMapper bookTypeMapper
    ) {
        CrudRepositoryEntityImpl<BookType, Integer> bookTypeRepository = new CrudRepositoryEntityImpl<>(bookTypeMapper);
        return new BookTypesService(bookTypeRepository);
    }
}
