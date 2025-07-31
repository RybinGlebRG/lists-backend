package ru.rerumu.lists.dao.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rerumu.lists.dao.book.AuthorsBooksRepository;
import ru.rerumu.lists.dao.book.mapper.BookMapper;
import ru.rerumu.lists.dao.book.BookRepository;
import ru.rerumu.lists.dao.book.impl.BookRepositoryImpl;
import ru.rerumu.lists.dao.series.SeriesBooksRespository;
import ru.rerumu.lists.dao.series.SeriesRepository;
import ru.rerumu.lists.dao.series.mapper.SeriesBookMapper;
import ru.rerumu.lists.dao.series.mapper.SeriesMapper;

@Configuration
public class BookRepositoryConfig {

    @Bean
    public BookRepository getBookRepository(
            BookMapper bookMapper,
            AuthorsBooksRepository authorsBooksRepository,
            SeriesMapper seriesMapper,
            SeriesBooksRespository seriesBooksRespository,
            SeriesRepository seriesRepository
    ) {
        return new BookRepositoryImpl(
                bookMapper,
                authorsBooksRepository,
                seriesMapper,
                seriesBooksRespository,
                seriesRepository
        );
    }
}
