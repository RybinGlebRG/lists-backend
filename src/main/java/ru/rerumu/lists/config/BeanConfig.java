package ru.rerumu.lists.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.BookTypeMapper;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.impl.BookRepositoryImpl;
import ru.rerumu.lists.repository.impl.CrudRepositoryEntityImpl;
import ru.rerumu.lists.services.BookTypesService;

@Configuration
public class BeanConfig {

    @Bean
    public BookRepository getBookRepository(
            BookMapper bookMapper,
            BookTypeMapper bookTypeMapper
            ){
        CrudRepositoryEntityImpl<BookType,Integer> bookTypeRepository1 = new CrudRepositoryEntityImpl<>(bookTypeMapper);
        return new BookRepositoryImpl(
                bookMapper,
                bookTypeRepository1
        );
    }

    @Bean
    public BookTypesService getBookTypesService(
            BookTypeMapper bookTypeMapper
    ){
        CrudRepositoryEntityImpl<BookType,Integer> bookTypeRepository = new CrudRepositoryEntityImpl<>(bookTypeMapper);
        return new BookTypesService(bookTypeRepository);
    }
}
