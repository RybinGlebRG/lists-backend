package ru.rerumu.lists.config.beans;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.dao.book.BookMapper;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.book.impl.BookRepositoryImpl;

@Configuration
public class BookRepositoryConfig {

    @Bean
    public BookRepository getBookRepository(
            BookMapper bookMapper
    ) {
        return new BookRepositoryImpl(
                bookMapper
        );
    }
}
