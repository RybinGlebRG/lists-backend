package ru.rerumu.lists.config.beans;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.mappers.BookMapper;
import ru.rerumu.lists.mappers.BookTypeMapper;
import ru.rerumu.lists.mappers.ReadingRecordMapper;
import ru.rerumu.lists.model.BookType;
import ru.rerumu.lists.repository.BookRepository;
import ru.rerumu.lists.repository.impl.BookRepositoryImpl;
import ru.rerumu.lists.repository.impl.CrudRepositoryDtoImpl;
import ru.rerumu.lists.repository.impl.CrudRepositoryEntityImpl;

@Configuration
public class BookRepositoryConfig {

    @Bean
    public BookRepository getBookRepository(
            BookMapper bookMapper,
            ReadingRecordMapper readingRecordMapper
    ) {
        return new BookRepositoryImpl(
                bookMapper,
                 readingRecordMapper
        );
    }
}
