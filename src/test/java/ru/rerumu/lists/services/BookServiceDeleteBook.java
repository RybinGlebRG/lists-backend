package ru.rerumu.lists.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.exception.EntityNotFoundException;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.repository.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceDeleteBook {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private SeriesRepository seriesRepository;
    @Mock
    private AuthorsRepository authorsRepository;
    @Mock
    private AuthorsService authorsService;
    @Mock
    private AuthorsBooksRepository authorsBooksRepository;
    @Mock
    private SeriesBooksRespository seriesBooksRespository;

    @Mock
    private DateFactory dateFactory;

    @Mock
    private BookSeriesService bookSeriesService;

    @Mock
    private BookSeriesRelationService bookSeriesRelationService;

    @Mock
    private  AuthorsBooksRelationService authorsBooksRelationService;


    @Test
    void shouldDeleteBook() throws Exception{
        long bookId = 1L;
        Date dt = new Date();
        Book book = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .bookStatus(BookStatus.COMPLETED)
                .lastChapter(7)
                .readListId(5L)
                .build()
                ;
        Author author = new Author(1L, 5L, "Author");
        Series series = new Series(3L,5L,"Series");

        when(bookRepository.getOne(anyLong())).thenReturn(Optional.of(book));
        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of(new SeriesBookRelation(book,series,1L))
        );
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of(new AuthorBookRelation(book,author))
        );


        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesService,
                bookSeriesRelationService,
                authorsBooksRelationService
        );
        readListService.deleteBook(bookId);


        verify(bookSeriesRelationService).delete(6L,3L,5L);
        verify(authorsBooksRelationService).delete(6L,1L,5L);
        verify(bookRepository).delete(6L);
    }

    @Test
    void shouldThrowException() throws Exception{
        long bookId = 1L;

        when(bookRepository.getOne(anyLong())).thenReturn(Optional.empty());


        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesService,
                bookSeriesRelationService,
                authorsBooksRelationService
        );

        Assertions.assertThrows(EntityNotFoundException.class,() -> readListService.deleteBook(bookId));
    }

}