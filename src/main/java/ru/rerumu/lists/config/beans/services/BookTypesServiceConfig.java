package ru.rerumu.lists.config.beans.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.dao.book.type.BookTypeMapper;
import ru.rerumu.lists.model.book.type.BookTypeDTO;
import ru.rerumu.lists.dao.base.impl.CrudRepositoryDtoImpl;
import ru.rerumu.lists.services.book.type.BookTypesService;

@Configuration
public class BookTypesServiceConfig {

    @Bean
    public BookTypesService getBookTypesService(
            BookTypeMapper bookTypeMapper
    ) {
        CrudRepositoryDtoImpl<BookTypeDTO, Integer> bookTypeRepository = new CrudRepositoryDtoImpl<>(bookTypeMapper);
        return new BookTypesService(bookTypeRepository);
    }
}
