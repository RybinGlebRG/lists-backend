package ru.rerumu.lists.config.beans;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.ReadingRecordMapper;
import ru.rerumu.lists.model.book.BookFactory;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.impl.BookRepositoryImpl;

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
