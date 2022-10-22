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
class BookServiceUpdateBookUpdateAuthorTest {

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
    void shouldChangeAuthor() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "Title",
                4L,
                1,
                6L,
                7L,
                4,
                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
                null
        );
//        Series series = new Series(5L,3L,"Series");
//        Series shouldSeries = new Series(6L,3L,"SeriesNew");
        Author author = new Author(2L, 3L,"Author");
        Author shouldAuthor = new Author(4L, 3L,"AuthorNew");
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
        when(authorsBooksRepository.getByBookId(anyLong(),anyLong())).thenReturn(
                List.of(new AuthorBookRelation(book,author))
        );
        when(authorsService.getAuthor(anyLong(),anyLong())).thenReturn(Optional.of(shouldAuthor));

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

        readListService.updateBook(8L,bookUpdateView);


        verify(authorsBooksRelationService).delete(8L,2L,3L);
        verify(authorsBooksRepository).add(8L,4L,3L);
    }

    @Test
    void shouldAddAuthor() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "Title",
                4L,
                1,
                6L,
                7L,
                4,
                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
                null
        );
//        Series series = new Series(5L,3L,"Series");
//        Series shouldSeries = new Series(6L,3L,"SeriesNew");
//        Author author = new Author(2L, 3L,"Author");
        Author shouldAuthor = new Author(4L, 3L,"AuthorNew");
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
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of()
        );
        when(authorsService.getAuthor(anyLong(),anyLong())).thenReturn(Optional.of(shouldAuthor));

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

        readListService.updateBook(8L,bookUpdateView);


        verify(authorsBooksRelationService, never()).delete(anyLong(),anyLong(),anyLong());
        verify(authorsBooksRepository).add(8L,4L,3L);
    }

    @Test
    void shouldRemoveAuthor() throws Exception{
        BookUpdateView bookUpdateView = new BookUpdateView(
                3L,
                "Title",
                null,
                1,
                6L,
                7L,
                4,
                LocalDateTime.of(2000, 10, 1, 0, 0, 0),
                null
        );
//        Series series = new Series(5L,3L,"Series");
//        Series shouldSeries = new Series(6L,3L,"SeriesNew");
        Author author = new Author(2L, 3L,"Author");
//        Author shouldAuthor = new Author(4L, 3L,"AuthorNew");
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
        when(authorsBooksRepository.getByBookId(anyLong(), anyLong())).thenReturn(
                List.of(new AuthorBookRelation(book,author))
        );
//        when(authorsService.getAuthor(anyLong(),anyLong())).thenReturn(Optional.empty());

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

        readListService.updateBook(8L,bookUpdateView);


        verify(authorsBooksRelationService).delete(8L,2L,3L);
        verify(authorsBooksRepository, never()).add(anyLong(),anyLong(),anyLong());
    }

}