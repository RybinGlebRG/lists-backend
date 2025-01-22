package ru.rerumu.lists.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rerumu.lists.factories.DateFactory;
import ru.rerumu.lists.model.*;
import ru.rerumu.lists.model.book.BookFactory;
import ru.rerumu.lists.model.book.BookImpl;
import ru.rerumu.lists.repository.*;
import ru.rerumu.lists.views.BookUpdateView;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceUpdateBookUpdateTypeTest {

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
    @Mock
    ReadingRecordService readingRecordService;
    @Mock
    BookFactory bookFactory;

    @Test
    void shouldUpdateType() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "TitleNew",
                null,
                1,
                null,
                null,
                null,
                LocalDateTime.of(2020, 10, 1, 0, 0, 0),
                2,
                null
        );
        BookImpl book = new BookImpl.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .readListId(3L)
                .bookType(new BookType(1,"Book"))
                .build();
        BookImpl shouldBook = new BookImpl.Builder()
                .bookId(8L)
                .title("TitleNew")
                .insertDate(Date.from(LocalDateTime.of(2020, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .readListId(3L)
                .bookType(new BookType(2,"Light Novel"))
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());
//        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());
        when(bookTypesService.findById(anyInt())).thenReturn(Optional.of(new BookType(2,"Light Novel")));
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
                fuzzyMatchingService,
                readingRecordService,
                bookFactory
        );

        readListService.updateBook(8L,bookUpdateView);

        verify(bookRepository).update(shouldBook);
    }

    @Test
    void shouldAddType() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "TitleNew",
                null,
                1,
                null,
                null,
                null,
                LocalDateTime.of(2020, 10, 1, 0, 0, 0),
                2,
                null
        );
        BookImpl book = new BookImpl.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .readListId(3L)
                .build();
        BookImpl shouldBook = new BookImpl.Builder()
                .bookId(8L)
                .title("TitleNew")
                .insertDate(Date.from(LocalDateTime.of(2020, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .readListId(3L)
                .bookType(new BookType(2,"Light Novel"))
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());
//        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());
        when(bookTypesService.findById(anyInt())).thenReturn(Optional.of(new BookType(2,"Light Novel")));
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
                fuzzyMatchingService,
                readingRecordService,
                bookFactory
        );

        readListService.updateBook(8L,bookUpdateView);

        verify(bookRepository).update(shouldBook);
    }

    @Test
    void shouldRemoveType() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "TitleNew",
                null,
                1,
                null,
                null,
                null,
                LocalDateTime.of(2020, 10, 1, 0, 0, 0),
                null,
                null
        );
        BookImpl book = new BookImpl.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
                .readListId(3L)
                .bookType(new BookType(1,"Book"))
                .build();
        BookImpl shouldBook = new BookImpl.Builder()
                .bookId(8L)
                .title("TitleNew")
                .insertDate(Date.from(LocalDateTime.of(2020, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(new BookStatusRecord(1,"In Progress"))
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
                fuzzyMatchingService,
                readingRecordService,
                bookFactory
        );

        readListService.updateBook(8L,bookUpdateView);

        verify(bookRepository).update(shouldBook);
    }

}