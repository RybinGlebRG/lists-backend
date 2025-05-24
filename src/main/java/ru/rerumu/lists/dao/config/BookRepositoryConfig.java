package ru.rerumu.lists.dao.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.mapper.BookMapper;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.book.impl.BookRepositoryImpl;

@Configuration
public class BookRepositoryConfig {

    @Bean
    public BookRepository getBookRepository(
            BookMapper bookMapper,
            AuthorsBooksRepository authorsBooksRepository
    ) {
        return new BookRepositoryImpl(
                bookMapper,
                 authorsBooksRepository
        );
    }
}
