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

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceDeleteBookTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorsService authorsService;
    @Mock
    private AuthorsBooksRepository authorsBooksRepository;
    @Mock
    private SeriesBooksRespository seriesBooksRespository;

    @Mock
    private DateFactory dateFactory;

    @Mock
    private BookSeriesRelationService bookSeriesRelationService;

    @Mock
    private  AuthorsBooksRelationService authorsBooksRelationService;

    @Mock
    private BookTypesService bookTypesService;

    @Mock
    BookStatusesService bookStatusesService;


    @Test
    void shouldDeleteBook() throws Exception{
        long bookId = 1L;
        Date dt = new Date();
        Book book = new Book.Builder()
                .bookId(6L)
                .title("Test")
                .insertDate(dt)
                .lastUpdateDate(dt)
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .lastChapter(7)
                .readListId(5L)
                .build()
                ;
        Author author = new Author(1L, 5L, "Author");
        Series series = new Series.Builder().seriesId(3L).readListId(5L).title("Series").build();

        when(bookRepository.getOne(anyLong())).thenReturn(Optional.of(book));
        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of(new SeriesBookRelation(book,series,1L))
        );
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of(new AuthorBookRelation(book,author))
        );


        ReadListService readListService = new ReadListService(
                bookRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService,
                bookStatusesService
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
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService,
                bookStatusesService
        );

        Assertions.assertThrows(EntityNotFoundException.class,() -> readListService.deleteBook(bookId));
    }

}