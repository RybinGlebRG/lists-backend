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

    @Mock
    private  AuthorsBooksRelationService authorsBooksRelationService;

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
                LocalDateTime.of(2020, 10, 1, 0, 0, 0)
        );
        Author author = new Author(5L, 3L, "Author");
        Author shouldAuthor = new Author(6L, 3L, "AuthorNew");
        Series series = new Series(5L,3L,"Series");
        Series shouldSeries = new Series(6L,3L,"SeriesNew");
        Book book = new Book.Builder()
                .bookId(8L)
                .title("Title")
                .insertDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(BookStatus.IN_PROGRESS)
                .lastChapter(4)
                .readListId(3L)
                .build();
        Book shouldBook = new Book.Builder()
                .bookId(8L)
                .title("TitleNew")
                .insertDate(Date.from(LocalDateTime.of(2020, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .lastUpdateDate(Date.from(LocalDateTime.of(2000, 10, 1, 0, 0, 0).toInstant(ZoneOffset.UTC)))
                .bookStatus(BookStatus.COMPLETED)
                .lastChapter(8)
                .readListId(3L)
                .build();

        when(bookRepository.getOne(anyLong(),anyLong())).thenReturn(book);
        when(authorsBooksRepository.getByBookId(anyLong())).thenReturn(List.of());
        when(seriesBooksRespository.getByBookId(anyLong())).thenReturn(List.of());

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

        verify(bookRepository).update(shouldBook);
    }

}