package ru.rerumu.lists.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookUpdateView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceUpdateBookTest {

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
    @Mock
    FuzzyMatchingService fuzzyMatchingService;

    @Test
    void shouldUpdate() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "TitleNew",
                null,
                2,
                null,
                null,
                8,
                LocalDateTime.of(2020, 10, 1, 0, 0, 0),
                null,
                "test note 2"
        );
        LocalDateTime dt = LocalDateTime.now();

        Book book = new Book.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(LocalDateTime.of(2000, 10, 1, 0, 0, 0))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .lastChapter(4)
                .readListId(3L)
                .build();
        Book shouldBook = new Book.Builder()
                .bookId(8L)
                .title("TitleNew")
                .insertDate(Date.from(LocalDateTime.of(2020, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(dt)
                .bookStatus(new BookStatusRecord(2,"Completed"))
                .lastChapter(8)
                .readListId(3L)
                .note("test note 2")
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());
//        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());
        Mockito.when(dateFactory.getLocalDateTime()).thenReturn(dt);
        when(bookStatusesService.findById(anyInt()))
                .thenReturn(Optional.of(new BookStatusRecord(2,"Completed")));

        ReadListService readListService = new ReadListService(
                bookRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService,
                bookStatusesService,
                fuzzyMatchingService
        );

        readListService.updateBook(8L,bookUpdateView);

        verify(bookRepository).update(shouldBook);
    }


    @Test
    void shouldUpdateNoChapterStatus() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "TitleNew",
                null,
                1,
                null,
                null,
                8,
                LocalDateTime.of(2020, 10, 1, 0, 0, 0),
                null,
                null
        );

        Book book = new Book.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(LocalDateTime.of(2000, 10, 1, 0, 0, 0))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .lastChapter(8)
                .readListId(3L)
                .build();
        Book shouldBook = new Book.Builder()
                .bookId(8L)
                .title("TitleNew")
                .insertDate(Date.from(LocalDateTime.of(2020, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(LocalDateTime.of(2000, 10, 1, 0, 0, 0))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .lastChapter(8)
                .readListId(3L)
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());
//        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());
        when(bookStatusesService.findById(anyInt()))
                .thenReturn(Optional.of(new BookStatusRecord(1,"In Progress")));

        ReadListService readListService = new ReadListService(
                bookRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService,
                bookStatusesService,
                fuzzyMatchingService
        );

        readListService.updateBook(8L,bookUpdateView);

        verify(bookRepository).update(shouldBook);
    }

}