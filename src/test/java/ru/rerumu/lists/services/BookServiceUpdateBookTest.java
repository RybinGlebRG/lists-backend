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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookServiceUpdateBookTest {

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

//    @Test
//    void shouldUpdate() throws Exception{
//        BookUpdateView bookUpdateView = new BookUpdateView(
//                3L,
//                "TitleNew",
//                6L,
//                2,
//                5L,
//                7L,
//                8,
//                LocalDateTime.of(2020, 10, 1, 0, 0, 0)
//        );
//        Author author = new Author(5L, 3L, "Author");
//        Author shouldAuthor = new Author(6L, 3L, "AuthorNew");
//        Series series = new Series(5L,3L,"Series");
//        Series shouldSeries = new Series(6L,3L,"SeriesNew");
//        Book book = new Book.Builder()
//                .bookId(8L)
//                .title("Title")
//                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .statusId(1)
//                .bookStatus(BookStatus.IN_PROGRESS)
//                .lastChapter(4)
//                .readListId(3L)
//                .build();
//        Book shouldBook = new Book.Builder()
//                .bookId(8L)
//                .title("TitleNew")
//                .insertDate(Date.from(LocalDateTime.of(2020, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
//                .statusId(2)
//                .bookStatus(BookStatus.COMPLETED)
//                .lastChapter(8)
//                .readListId(3L)
//                .build();
//
//        when(bookRepository.getOne(anyLong(),anyLong()))
//                .thenReturn(book);
//        when(authorsBooksRepository.getByBookId(anyLong())).thenReturn(
//                List.of(new AuthorBookRelation(shouldBook,author))
//        );
//        when(authorsService.getAuthor(anyLong(),anyLong()))
//                .thenReturn(Optional.of(shouldAuthor));
//        when(seriesBooksRespository.getByBookId(anyLong())).thenReturn(
//                 List.of(new SeriesBookRelation(shouldBook,series))
//        );
//        when(bookSeriesService.getSeries(anyLong(),anyLong())).thenReturn(Optional.of(series));
//
//        ReadListService readListService = new ReadListService(
//                bookRepository,
//                seriesRepository,
//                authorsRepository,
//                authorsService,
//                authorsBooksRepository,
//                seriesBooksRespository,
//                dateFactory,
//                bookSeriesService
//        );
//
//        readListService.updateBook(8L,bookUpdateView);
//
//        verify(bookRepository).getOne(3L,8L);
//        verify(bookRepository).update(shouldBook);
//
//        verify(authorsBooksRepository).getByBookId(8L);
//        verify(authorsService).getAuthor(3L,6L);
//        verify(authorsBooksRepository).deleteByAuthor(5L);
//        verify(authorsBooksRepository).add(8L,6L,3L);
//
//        verify(seriesBooksRespository).getByBookId(8L);
//        verify(bookSeriesService).getSeries(3L,6L);
//        verify(seriesBooksRespository).deleteBySeries(5L);
//        verify(seriesBooksRespository).add(8L,6L,3L,7L);
//
//    }

    // TODO: Test update

}