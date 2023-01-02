package ru.rerumu.lists.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceUpdateBookUpdateSeriesTest {

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
    private SeriesService seriesService;

    @Mock
    private BookSeriesRelationService bookSeriesRelationService;

    @Mock
    private  AuthorsBooksRelationService authorsBooksRelationService;

    @Mock
    private BookTypesService bookTypesService;


    @Test
    void shouldUpdateChangeSeries() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "Title",
                6L,
                1,
                6L,
                7L,
                4,
                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
                null
        );
        Series series = new Series.Builder().seriesId(5L).readListId(3L).title("Series").build();
        Series shouldSeries = new Series.Builder().seriesId(6L).readListId(3L).title("SeriesNew").build();
        Book book = new Book.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(BookStatus.IN_PROGRESS)
                .lastChapter(4)
                .readListId(3L)
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());

        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(
                 List.of(new SeriesBookRelation(book,series,1L))
        );
        when(seriesService.getSeries(anyLong(),anyLong())).thenReturn(Optional.of(shouldSeries));

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );

        readListService.updateBook(8L,bookUpdateView);

        verify(bookRepository).getOne(3L,8L);
        verify(bookRepository).update(book);

        verify(bookSeriesRelationService).delete(8L,5L,3L);
        verify(seriesBooksRespository).add(8L,6L,3L,7L);

    }

    @Test
    void shouldUpdateAddSeries() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "Title",
                6L,
                1,
                6L,
                7L,
                4,
                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
                null
        );
//        Author author = new Author(5L, 3L, "Author");
//        Series series = new Series(5L,3L,"Series");
        Series shouldSeries = new Series.Builder().seriesId(6L).readListId(3L).title("SeriesNew").build();
        Book book = new Book.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(BookStatus.IN_PROGRESS)
                .lastChapter(4)
                .readListId(3L)
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());

        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of()
        );
        when(seriesService.getSeries(anyLong(),anyLong())).thenReturn(Optional.of(shouldSeries));

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );

        readListService.updateBook(8L,bookUpdateView);

        verify(bookRepository).getOne(3L,8L);
        verify(bookRepository).update(book);

        verify(bookSeriesRelationService, never()).delete(anyLong(),anyLong(),anyLong());
        verify(seriesBooksRespository).add(8L,6L,3L,7L);

    }

    @Test
    void shouldUpdateRemoveSeries() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "Title",
                6L,
                1,
                null,
                null,
                4,
                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
                null
        );
//        Author author = new Author(5L, 3L, "Author");
        Series series = new Series.Builder().seriesId(5L).readListId(3L).title("Series").build();
//        Series shouldSeries = new Series(6L,3L,"SeriesNew");
        Book book = new Book.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(BookStatus.IN_PROGRESS)
                .lastChapter(4)
                .readListId(3L)
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());

        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of(new SeriesBookRelation(book,series,1L))
        );
//        when(bookSeriesService.getSeries(anyLong(),anyLong())).thenReturn(Optional.of(shouldSeries));

        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );

        readListService.updateBook(8L,bookUpdateView);

        verify(bookRepository).getOne(3L,8L);
        verify(bookRepository).update(book);

        verify(bookSeriesRelationService).delete(8L,5L,3L);
        verify(seriesBooksRespository, never()).add(anyLong(),anyLong(),anyLong(),anyLong());

    }

    @Test
    void shouldUpdateChangeOrder() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "Title",
                6L,
                1,
                5L,
                7L,
                4,
                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
                null
        );
        Series series = new Series.Builder().seriesId(5L).readListId(3L).title("Series").build();
        Book book = new Book.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(BookStatus.IN_PROGRESS)
                .lastChapter(4)
                .readListId(3L)
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(List.of());

        when(seriesBooksRespository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of(new SeriesBookRelation(book,series,1L))
        );
        when(seriesService.getSeries(anyLong(),anyLong())).thenReturn(Optional.of(series));


        ReadListService readListService = new ReadListService(
                bookRepository,
                seriesRepository,
                authorsRepository,
                authorsService,
                authorsBooksRepository,
                seriesBooksRespository,
                dateFactory,
                seriesService,
                bookSeriesRelationService,
                authorsBooksRelationService,
                bookTypesService
        );
        readListService.updateBook(8L,bookUpdateView);


        verify(bookSeriesRelationService,never()).delete(anyLong(),anyLong(),anyLong());
        verify(seriesBooksRespository, never()).add(anyLong(),anyLong(),anyLong(),anyLong());
        verify(bookSeriesRelationService).update(
                new SeriesBookRelation(book,series,7L)
        );

    }

}